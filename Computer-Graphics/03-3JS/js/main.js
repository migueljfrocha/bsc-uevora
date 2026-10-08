'use strict';
import { Wall } from './Objects/Wall.js';
import { Floor } from './Objects/Floor.js';
import { Word } from './Objects/Word.js';

/**
 *  Make a model of the Word
 */
function model() {
    const word = new Word();
    return word;
}

function animate(step) {
    requestAnimationFrame(function () {
        animate(step);
    });
    TWEEN.update();
    step.controls.update();
    step.renderer.render(step.scene, step.camera);
}
function init(mesh) {
    
    let renderer = new THREE.WebGLRenderer({
        alpha: true,
        antialias: true
    });

    let size = Math.min(parent.innerWidth, 800);
    renderer.setSize(size, size);
    //
    // Get the context
    //
    let div_container = document.getElementById("container");
    div_container.appendChild(renderer.domElement);
    //
    // Create the scene
    //
    let scene = new THREE.Scene();
    //
    // Create the camera
    //
    let camera = new THREE.PerspectiveCamera(
        45,
        1,
        0.1,
        10000
    );
    camera.position.set(0, -10, 280);
    camera.lookAt(scene.position);

    let controls = new THREE.OrbitControls(camera, renderer.domElement);

    // Add light to the scene
    let ambient_light = new THREE.AmbientLight(0xFFFFFF);
    scene.add(ambient_light);

    // Add the mesh to the scene
    scene.add(mesh);
    //
    // Add the other objects to the scene
    //
    const floor = new Floor();
    scene.add(floor);
    const wall = new Wall();
    scene.add(wall);

    return {
        camera: camera,
        scene: scene,
        renderer: renderer,
        controls: controls
    };
}

function main() {
    let mesh = model();
    let step = init(mesh);
    animate(step);
}

main();
