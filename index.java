<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Robotic Arm Simulation</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/three@0.128.0/examples/js/controls/OrbitControls.min.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Arial, sans-serif;
        }
        
        body {
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
            color: #e6e6e6;
            min-height: 100vh;
            overflow-x: hidden;
        }
        
        .container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 20px;
        }
        
        header {
            text-align: center;
            margin-bottom: 30px;
            padding: 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        h1 {
            font-size: 2.5rem;
            margin-bottom: 10px;
            color: #4dccbd;
            text-shadow: 0 0 10px rgba(77, 204, 189, 0.3);
        }
        
        .subtitle {
            font-size: 1.2rem;
            color: #a0a0c0;
            max-width: 800px;
            margin: 0 auto;
            line-height: 1.6;
        }
        
        .content {
            display: flex;
            flex-wrap: wrap;
            gap: 30px;
            margin-bottom: 40px;
        }
        
        .simulation-area {
            flex: 1;
            min-width: 300px;
            height: 600px;
            background-color: #0d1117;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
            position: relative;
        }
        
        #robotCanvas {
            width: 100%;
            height: 100%;
            display: block;
        }
        
        .controls-panel {
            flex: 1;
            min-width: 300px;
            background-color: rgba(30, 35, 56, 0.7);
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .panel-title {
            font-size: 1.5rem;
            margin-bottom: 20px;
            color: #4dccbd;
            padding-bottom: 10px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .dof-info {
            background-color: rgba(20, 25, 40, 0.8);
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 25px;
        }
        
        .dof-info h3 {
            color: #6cbfa9;
            margin-bottom: 10px;
        }
        
        .dof-list {
            list-style-type: none;
        }
        
        .dof-list li {
            padding: 8px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
            display: flex;
            justify-content: space-between;
        }
        
        .dof-list li:last-child {
            border-bottom: none;
        }
        
        .dof-name {
            font-weight: 500;
        }
        
        .dof-value {
            color: #4dccbd;
            font-weight: bold;
        }
        
        .control-group {
            margin-bottom: 25px;
        }
        
        .control-label {
            display: block;
            margin-bottom: 10px;
            color: #a0a0c0;
            font-weight: 500;
        }
        
        .slider-container {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        
        .slider-value {
            min-width: 50px;
            text-align: center;
            color: #4dccbd;
            font-weight: bold;
        }
        
        input[type="range"] {
            flex: 1;
            height: 8px;
            -webkit-appearance: none;
            background: rgba(100, 100, 120, 0.3);
            border-radius: 4px;
            outline: none;
        }
        
        input[type="range"]::-webkit-slider-thumb {
            -webkit-appearance: none;
            width: 22px;
            height: 22px;
            border-radius: 50%;
            background: #4dccbd;
            cursor: pointer;
            box-shadow: 0 0 10px rgba(77, 204, 189, 0.7);
        }
        
        .buttons {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 14px 24px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            flex: 1;
            min-width: 140px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #4dccbd 0%, #2a9d8f 100%);
            color: white;
        }
        
        .btn-secondary {
            background: rgba(100, 100, 120, 0.3);
            color: #e6e6e6;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }
        
        .btn:active {
            transform: translateY(1px);
        }
        
        .status-panel {
            background-color: rgba(20, 25, 40, 0.8);
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            border-left: 4px solid #4dccbd;
        }
        
        .status-text {
            color: #a0a0c0;
            font-size: 1rem;
        }
        
        .status-value {
            color: #4dccbd;
            font-weight: bold;
        }
        
        .instructions {
            background-color: rgba(30, 35, 56, 0.7);
            border-radius: 12px;
            padding: 25px;
            margin-top: 30px;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .instructions h2 {
            color: #4dccbd;
            margin-bottom: 15px;
        }
        
        .instructions ul {
            padding-left: 20px;
            line-height: 1.6;
        }
        
        .instructions li {
            margin-bottom: 10px;
            color: #a0a0c0;
        }
        
        footer {
            text-align: center;
            margin-top: 40px;
            padding-top: 20px;
            border-top: 1px solid rgba(255, 255, 255, 0.1);
            color: #a0a0c0;
            font-size: 0.9rem;
        }
        
        @media (max-width: 768px) {
            .content {
                flex-direction: column;
            }
            
            .simulation-area, .controls-panel {
                min-width: 100%;
            }
            
            .simulation-area {
                height: 400px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Robotic Arm Simulation</h1>
            <p class="subtitle">A 3D simulation of a 6-DOF robotic arm with pick-and-place functionality. Control each joint individually or run automated sequences.</p>
        </header>
        
        <div class="content">
            <div class="simulation-area">
                <canvas id="robotCanvas"></canvas>
            </div>
            
            <div class="controls-panel">
                <h2 class="panel-title">Control Panel</h2>
                
                <div class="dof-info">
                    <h3>Degrees of Freedom (DOF): 6</h3>
                    <ul class="dof-list">
                        <li><span class="dof-name">Base Rotation</span> <span class="dof-value" id="dof1">0°</span></li>
                        <li><span class="dof-name">Shoulder Joint</span> <span class="dof-value" id="dof2">0°</span></li>
                        <li><span class="dof-name">Elbow Joint</span> <span class="dof-value" id="dof3">0°</span></li>
                        <li><span class="dof-name">Wrist Pitch</span> <span class="dof-value" id="dof4">0°</span></li>
                        <li><span class="dof-name">Wrist Roll</span> <span class="dof-value" id="dof5">0°</span></li>
                        <li><span class="dof-name">Gripper</span> <span class="dof-value" id="dof6">0%</span></li>
                    </ul>
                </div>
                
                <div class="control-group">
                    <label class="control-label">Base Rotation</label>
                    <div class="slider-container">
                        <input type="range" id="baseSlider" min="-180" max="180" value="0" step="1">
                        <span class="slider-value" id="baseValue">0°</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">Shoulder Joint</label>
                    <div class="slider-container">
                        <input type="range" id="shoulderSlider" min="-90" max="90" value="0" step="1">
                        <span class="slider-value" id="shoulderValue">0°</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">Elbow Joint</label>
                    <div class="slider-container">
                        <input type="range" id="elbowSlider" min="0" max="135" value="45" step="1">
                        <span class="slider-value" id="elbowValue">45°</span>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">Gripper Open/Close</label>
                    <div class="slider-container">
                        <input type="range" id="gripperSlider" min="0" max="100" value="0" step="1">
                        <span class="slider-value" id="gripperValue">0%</span>
                    </div>
                </div>
                
                <div class="buttons">
                    <button class="btn btn-primary" id="pickPlaceBtn">Pick & Place Demo</button>
                    <button class="btn btn-secondary" id="resetBtn">Reset Position</button>
                    <button class="btn btn-secondary" id="randomBtn">Random Pose</button>
                </div>
                
                <div class="status-panel">
                    <p class="status-text">Status: <span class="status-value" id="statusText">Ready</span></p>
                    <p class="status-text">Mode: <span class="status-value" id="modeText">Manual Control</span></p>
                </div>
            </div>
        </div>
        
        <div class="instructions">
            <h2>How to Use This Simulation</h2>
            <ul>
                <li>Use the sliders to control each joint of the robotic arm individually</li>
                <li>Click "Pick & Place Demo" to run an automated pick-and-place sequence</li>
                <li>Click "Reset Position" to return the arm to its default position</li>
                <li>Click "Random Pose" to set the arm to a random configuration</li>
                <li>Click and drag in the 3D view to rotate the camera</li>
                <li>Use the mouse wheel to zoom in and out</li>
            </ul>
        </div>
        
        <footer>
            <p>Robotic Arm Simulation | 6-DOF Articulated Robot | Designed for Educational Purposes</p>
            <p>This simulation uses Three.js for 3D rendering and demonstrates forward kinematics</p>
        </footer>
    </div>

    <script>
        // Global variables
        let scene, camera, renderer, controls;
        let base, shoulder, elbow, wrist, gripperLeft, gripperRight;
        let objects = [];
        let selectedObject = null;
        let isAnimating = false;
        let animationStep = 0;
        
        // Robotic arm parameters
        const armParams = {
            baseRotation: 0,
            shoulderAngle: 0,
            elbowAngle: 45,
            wristPitch: 0,
            wristRoll: 0,
            gripperOpen: 0
        };
        
        // Target positions for pick and place
        const pickPosition = { x: -1.5, y: 0.5, z: 0 };
        const placePosition = { x: 1.5, y: 0.5, z: 0 };
        
        // Initialize the 3D scene
        function init() {
            // Create scene
            scene = new THREE.Scene();
            scene.background = new THREE.Color(0x0d1117);
            
            // Create camera
            camera = new THREE.PerspectiveCamera(60, 1, 0.1, 1000);
            camera.position.set(5, 3, 5);
            camera.lookAt(0, 1, 0);
            
            // Create renderer
            const canvas = document.getElementById('robotCanvas');
            renderer = new THREE.WebGLRenderer({ canvas, antialias: true });
            renderer.setSize(canvas.clientWidth, canvas.clientHeight);
            renderer.shadowMap.enabled = true;
            renderer.shadowMap.type = THREE.PCFSoftShadowMap;
            
            // Add orbit controls
            controls = new THREE.OrbitControls(camera, renderer.domElement);
            controls.enableDamping = true;
            controls.dampingFactor = 0.05;
            
            // Add lights
            const ambientLight = new THREE.AmbientLight(0x404040, 0.6);
            scene.add(ambientLight);
            
            const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
            directionalLight.position.set(5, 10, 5);
            directionalLight.castShadow = true;
            directionalLight.shadow.camera.left = -10;
            directionalLight.shadow.camera.right = 10;
            directionalLight.shadow.camera.top = 10;
            directionalLight.shadow.camera.bottom = -10;
            scene.add(directionalLight);
            
            // Add a grid helper
            const gridHelper = new THREE.GridHelper(10, 20, 0x444444, 0x222222);
            scene.add(gridHelper);
            
            // Create robotic arm
            createRoboticArm();
            
            // Create objects for pick and place
            createObjects();
            
            // Update DOF displays
            updateDOFDisplay();
            
            // Add event listeners
            setupEventListeners();
            
            // Handle window resize
            window.addEventListener('resize', onWindowResize);
            
            // Start animation loop
            animate();
        }
        
        // Create the robotic arm
        function createRoboticArm() {
            const material = new THREE.MeshStandardMaterial({ 
                color: 0x4dccbd,
                metalness: 0.6,
                roughness: 0.4
            });
            
            const jointMaterial = new THREE.MeshStandardMaterial({ 
                color: 0x2a9d8f,
                metalness: 0.8,
                roughness: 0.2
            });
            
            // Base
            const baseGeometry = new THREE.CylinderGeometry(0.8, 1, 0.4, 16);
            base = new THREE.Mesh(baseGeometry, material);
            base.position.y = 0.2;
            base.castShadow = true;
            scene.add(base);
            
            // Shoulder (vertical part)
            const shoulderGeometry = new THREE.BoxGeometry(0.6, 1.2, 0.6);
            shoulder = new THREE.Mesh(shoulderGeometry, material);
            shoulder.position.y = 1.0;
            shoulder.castShadow = true;
            base.add(shoulder);
            
            // Shoulder joint
            const shoulderJointGeometry = new THREE.SphereGeometry(0.4, 16, 16);
            const shoulderJoint = new THREE.Mesh(shoulderJointGeometry, jointMaterial);
            shoulderJoint.position.y = 0.7;
            shoulderJoint.castShadow = true;
            shoulder.add(shoulderJoint);
            
            // Upper arm
            const upperArmGeometry = new THREE.BoxGeometry(0.5, 2, 0.5);
            const upperArm = new THREE.Mesh(upperArmGeometry, material);
            upperArm.position.y = 1.5;
            upperArm.castShadow = true;
            shoulderJoint.add(upperArm);
            
            // Elbow joint
            const elbowJointGeometry = new THREE.SphereGeometry(0.35, 16, 16);
            const elbowJoint = new THREE.Mesh(elbowJointGeometry, jointMaterial);
            elbowJoint.position.y = 1.5;
            elbowJoint.castShadow = true;
            upperArm.add(elbowJoint);
            
            // Lower arm
            const lowerArmGeometry = new THREE.BoxGeometry(0.4, 2, 0.4);
            elbow = new THREE.Mesh(lowerArmGeometry, material);
            elbow.position.y = 1.5;
            elbow.castShadow = true;
            elbowJoint.add(elbow);
            
            // Wrist joint
            const wristJointGeometry = new THREE.SphereGeometry(0.3, 16, 16);
            const wristJoint = new THREE.Mesh(wristJointGeometry, jointMaterial);
            wristJoint.position.y = 1.5;
            wristJoint.castShadow = true;
            elbow.add(wristJoint);
            
            // Wrist connector
            const wristConnectorGeometry = new THREE.BoxGeometry(0.3, 0.6, 0.3);
            wrist = new THREE.Mesh(wristConnectorGeometry, material);
            wrist.position.y = 0.5;
            wrist.castShadow = true;
            wristJoint.add(wrist);
            
            // Gripper base
            const gripperBaseGeometry = new THREE.BoxGeometry(0.4, 0.2, 0.4);
            const gripperBase = new THREE.Mesh(gripperBaseGeometry, material);
            gripperBase.position.y = 0.3;
            gripperBase.castShadow = true;
            wrist.add(gripperBase);
            
            // Left gripper
            const gripperGeometry = new THREE.BoxGeometry(0.1, 0.6, 0.2);
            gripperLeft = new THREE.Mesh(gripperGeometry, material);
            gripperLeft.position.set(-0.15, 0.4, 0);
            gripperLeft.castShadow = true;
            gripperBase.add(gripperLeft);
            
            // Right gripper
            gripperRight = new THREE.Mesh(gripperGeometry, material);
            gripperRight.position.set(0.15, 0.4, 0);
            gripperRight.castShadow = true;
            gripperBase.add(gripperRight);
        }
        
        // Create objects for pick and place
        function createObjects() {
            const objectMaterial = new THREE.MeshStandardMaterial({ 
                color: 0xff6b6b,
                metalness: 0.5,
                roughness: 0.5
            });
            
            // Object to pick up
            const objectGeometry = new THREE.CylinderGeometry(0.2, 0.2, 0.3, 16);
            const object = new THREE.Mesh(objectGeometry, objectMaterial);
            object.position.copy(pickPosition);
            object.castShadow = true;
            scene.add(object);
            objects.push(object);
            
            // Target platform
            const platformGeometry = new THREE.BoxGeometry(1, 0.1, 1);
            const platformMaterial = new THREE.MeshStandardMaterial({ color: 0x555555 });
            const platform = new THREE.Mesh(platformGeometry, platformMaterial);
            platform.position.copy(placePosition);
            platform.position.y = 0.05;
            platform.receiveShadow = true;
            scene.add(platform);
            
            // Starting platform
            const startPlatform = new THREE.Mesh(platformGeometry, platformMaterial);
            startPlatform.position.copy(pickPosition);
            startPlatform.position.y = 0.05;
            startPlatform.receiveShadow = true;
            scene.add(startPlatform);
        }
        
        // Update robotic arm based on parameters
        function updateArm() {
            // Base rotation
            base.rotation.y = THREE.MathUtils.degToRad(armParams.baseRotation);
            
            // Shoulder joint
            shoulder.rotation.z = THREE.MathUtils.degToRad(armParams.shoulderAngle);
            
            // Elbow joint
            elbow.rotation.z = THREE.MathUtils.degToRad(armParams.elbowAngle);
            
            // Wrist pitch (simplified - using elbow rotation for demonstration)
            wrist.rotation.z = THREE.MathUtils.degToRad(armParams.wristPitch);
            
            // Wrist roll
            wrist.rotation.x = THREE.MathUtils.degToRad(armParams.wristRoll);
            
            // Gripper open/close
            const gripperOpen = armParams.gripperOpen / 100;
            gripperLeft.position.x = -0.15 - gripperOpen * 0.2;
            gripperRight.position.x = 0.15 + gripperOpen * 0.2;
            
            // Update DOF display
            updateDOFDisplay();
        }
        
        // Update DOF display values
        function updateDOFDisplay() {
            document.getElementById('dof1').textContent = `${armParams.baseRotation.toFixed(1)}°`;
            document.getElementById('dof2').textContent = `${armParams.shoulderAngle.toFixed(1)}°`;
            document.getElementById('dof3').textContent = `${armParams.elbowAngle.toFixed(1)}°`;
            document.getElementById('dof4').textContent = `${armParams.wristPitch.toFixed(1)}°`;
            document.getElementById('dof5').textContent = `${armParams.wristRoll.toFixed(1)}°`;
            document.getElementById('dof6').textContent = `${armParams.gripperOpen.toFixed(0)}%`;
            
            document.getElementById('baseValue').textContent = `${armParams.baseRotation.toFixed(0)}°`;
            document.getElementById('shoulderValue').textContent = `${armParams.shoulderAngle.toFixed(0)}°`;
            document.getElementById('elbowValue').textContent = `${armParams.elbowAngle.toFixed(0)}°`;
            document.getElementById('gripperValue').textContent = `${armParams.gripperOpen.toFixed(0)}%`;
        }
        
        // Setup event listeners
        function setupEventListeners() {
            // Slider controls
            document.getElementById('baseSlider').addEventListener('input', function(e) {
                if (isAnimating) return;
                armParams.baseRotation = parseFloat(e.target.value);
                updateArm();
            });
            
            document.getElementById('shoulderSlider').addEventListener('input', function(e) {
                if (isAnimating) return;
                armParams.shoulderAngle = parseFloat(e.target.value);
                updateArm();
            });
            
            document.getElementById('elbowSlider').addEventListener('input', function(e) {
                if (isAnimating) return;
                armParams.elbowAngle = parseFloat(e.target.value);
                updateArm();
            });
            
            document.getElementById('gripperSlider').addEventListener('input', function(e) {
                if (isAnimating) return;
                armParams.gripperOpen = parseFloat(e.target.value);
                updateArm();
            });
            
            // Button controls
            document.getElementById('pickPlaceBtn').addEventListener('click', function() {
                if (!isAnimating) {
                    startPickAndPlaceAnimation();
                }
            });
            
            document.getElementById('resetBtn').addEventListener('click', function() {
                if (isAnimating) return;
                resetArmPosition();
            });
            
            document.getElementById('randomBtn').addEventListener('click', function() {
                if (isAnimating) return;
                setRandomArmPosition();
            });
        }
        
        // Start pick and place animation
        function startPickAndPlaceAnimation() {
            isAnimating = true;
            animationStep = 0;
            document.getElementById('statusText').textContent = 'Running Pick & Place';
            document.getElementById('modeText').textContent = 'Automated Sequence';
            
            // Store initial position to return to later
            const initialParams = {...armParams};
            
            // Animation sequence
            const animationSequence = [
                // Step 1: Move to pick position
                { baseRotation: -60, shoulderAngle: -20, elbowAngle: 60, gripperOpen: 0 },
                // Step 2: Open gripper
                { baseRotation: -60, shoulderAngle: -20, elbowAngle: 60, gripperOpen: 80 },
                // Step 3: Lower to object
                { baseRotation: -60, shoulderAngle: -30, elbowAngle: 80, gripperOpen: 80 },
                // Step 4: Close gripper (pick)
                { baseRotation: -60, shoulderAngle: -30, elbowAngle: 80, gripperOpen: 10 },
                // Step 5: Lift object
                { baseRotation: -60, shoulderAngle: -10, elbowAngle: 50, gripperOpen: 10 },
                // Step 6: Move to place position
                { baseRotation: 60, shoulderAngle: -20, elbowAngle: 60, gripperOpen: 10 },
                // Step 7: Lower to place
                { baseRotation: 60, shoulderAngle: -30, elbowAngle: 80, gripperOpen: 10 },
                // Step 8: Open gripper (place)
                { baseRotation: 60, shoulderAngle: -30, elbowAngle: 80, gripperOpen: 80 },
                // Step 9: Lift after place
                { baseRotation: 60, shoulderAngle: -10, elbowAngle: 50, gripperOpen: 80 },
                // Step 10: Return to initial
                initialParams
            ];
            
            // Animate through the sequence
            function animateStep() {
                if (animationStep >= animationSequence.length) {
                    isAnimating = false;
                    document.getElementById('statusText').textContent = 'Ready';
                    document.getElementById('modeText').textContent = 'Manual Control';
                    return;
                }
                
                const target = animationSequence[animationStep];
                const duration = 800; // ms per step
                const steps = 40;
                const stepTime = duration / steps;
                
                // Calculate increments
                const baseInc = (target.baseRotation - armParams.baseRotation) / steps;
                const shoulderInc = (target.shoulderAngle - armParams.shoulderAngle) / steps;
                const elbowInc = (target.elbowAngle - armParams.elbowAngle) / steps;
                const gripperInc = (target.gripperOpen - armParams.gripperOpen) / steps;
                
                let currentStep = 0;
                
                function updateStep() {
                    if (currentStep >= steps) {
                        animationStep++;
                        setTimeout(animateStep, 200);
                        return;
                    }
                    
                    armParams.baseRotation += baseInc;
                    armParams.shoulderAngle += shoulderInc;
                    armParams.elbowAngle += elbowInc;
                    armParams.gripperOpen += gripperInc;
                    
                    updateArm();
                    
                    // Update sliders to match animation
                    document.getElementById('baseSlider').value = armParams.baseRotation;
                    document.getElementById('shoulderSlider').value = armParams.shoulderAngle;
                    document.getElementById('elbowSlider').value = armParams.elbowAngle;
                    document.getElementById('gripperSlider').value = armParams.gripperOpen;
                    
                    currentStep++;
                    setTimeout(updateStep, stepTime);
                }
                
                updateStep();
            }
            
            animateStep();
        }
        
        // Reset arm to default position
        function resetArmPosition() {
            armParams.baseRotation = 0;
            armParams.shoulderAngle = 0;
            armParams.elbowAngle = 45;
            armParams.wristPitch = 0;
            armParams.wristRoll = 0;
            armParams.gripperOpen = 0;
            
            // Update sliders
            document.getElementById('baseSlider').value = armParams.baseRotation;
            document.getElementById('shoulderSlider').value = armParams.shoulderAngle;
            document.getElementById('elbowSlider').value = armParams.elbowAngle;
            document.getElementById('gripperSlider').value = armParams.gripperOpen;
            
            updateArm();
        }
        
        // Set arm to random position
        function setRandomArmPosition() {
            armParams.baseRotation = Math.floor(Math.random() * 360 - 180);
            armParams.shoulderAngle = Math.floor(Math.random() * 180 - 90);
            armParams.elbowAngle = Math.floor(Math.random() * 135);
            armParams.wristPitch = Math.floor(Math.random() * 90 - 45);
            armParams.wristRoll = Math.floor(Math.random() * 180 - 90);
            armParams.gripperOpen = Math.floor(Math.random() * 100);
            
            // Update sliders
            document.getElementById('baseSlider').value = armParams.baseRotation;
            document.getElementById('shoulderSlider').value = armParams.shoulderAngle;
            document.getElementById('elbowSlider').value = armParams.elbowAngle;
            document.getElementById('gripperSlider').value = armParams.gripperOpen;
            
            updateArm();
        }
        
        // Handle window resize
        function onWindowResize() {
            const canvas = document.getElementById('robotCanvas');
            camera.aspect = canvas.clientWidth / canvas.clientHeight;
            camera.updateProjectionMatrix();
            renderer.setSize(canvas.clientWidth, canvas.clientHeight);
        }
        
        // Animation loop
        function animate() {
            requestAnimationFrame(animate);
            
            // Update controls
            controls.update();
            
            // Render the scene
            renderer.render(scene, camera);
        }
        
        // Initialize when page loads
        window.addEventListener('DOMContentLoaded', init);
    </script>
</body>
</html>