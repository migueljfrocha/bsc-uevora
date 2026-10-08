import { LetterA } from './LetterA.js';
import { LetterA_accent } from './LetterA_accent.js';
import { LetterC } from './LetterC.js';
import { LetterD } from './LetterD.js';
import { LetterO } from './LetterO.js';
import { LetterU } from './LetterU.js';
import { LetterN } from './LetterN.js';

export class Word extends THREE.Group{

    constructor() {
        super();
        this.buildWord();
        this.position.z = -95;
        this.position.y = 80;
        this.position.x = -50;
        this.rotation.z = - Math.PI / 2;
        this.rotation.x = Math.PI;
        this.rotation.y = - Math.PI/2;
        this.startScalingAnimations();
    }
    //
    // Create the word COADUNÁ
    //
    buildWord() {
        const letters = [
            new LetterC(),
            new LetterO(),
            new LetterA(),
            new LetterD(),
            new LetterU(),
            new LetterN(),
            new LetterA_accent()
        ];

        letters.forEach((letter, index) => {
            const geometry = letter.getGeometry();

            const material = new THREE.MeshPhongMaterial({
                color: 0x000000,
                side: THREE.DoubleSide
            });

            const mesh = new THREE.Mesh(geometry, material);
            mesh.position.x = 200;

            this.add(mesh);

            this.createFallingAnimation(mesh, index);
            this.createExtrusionAnimation(mesh);
            this.createColorAnimation(mesh.material);
        });
    }
    //
    // Create the falling animation for each letter
    //
    createFallingAnimation(mesh, index) {
        const initialPosition = { x: 200 };
        const targetPosition = { x: 0 };

        const tween = new TWEEN.Tween(initialPosition)
            .to(targetPosition, 900)
            .delay(index * 700)
            .easing(TWEEN.Easing.Bounce.Out)
            .onUpdate(() => {
                mesh.position.x = initialPosition.x;
            })
            .start();
    }
    //
    // Change the color of the letters from black to red
    //
    createColorAnimation(material) {
        const initialColor = { r: 0, g: 0, b: 0 }; // Black
        const targetColor = { r: 1, g: 0, b: 0 }; // Red

        const colorTween = new TWEEN.Tween(initialColor)
            .to(targetColor, 4000)
            .easing(TWEEN.Easing.Linear.None)
            .delay(8000)
            .onUpdate(() => {
                material.color.setRGB(initialColor.r, initialColor.g, initialColor.b);
            })
            .yoyo(true)
            .repeat(Infinity)
            .start();
    }

    createExtrusionAnimation(mesh) {
        //
        //Animate Extrusion of the letters
        //
        mesh.doScaleY = new TWEEN.Tween(mesh.scale)
            .to({ y: mesh.scale.y + 4.0 }, 2000)
            .easing(TWEEN.Easing.Elastic.Out) 
            .delay(6000)
        ;
        mesh.getBackScaleY = new TWEEN.Tween(mesh.scale)
            .to({ y: mesh.scale.y }, 1000 * 2)
            .easing(TWEEN.Easing.Elastic.Out)
        ;

        mesh.doScaleY.chain(mesh.getBackScaleY);
        mesh.getBackScaleY.chain(mesh.doScaleY);

        mesh.doScaleY.start(); 
    }

    startScalingAnimations() {

        const initialY = this.position.y;
        const initialX = this.position.x;

        this.doScaleX = new TWEEN.Tween(this.scale)
            .to({ x: this.scale.x + 1 }, 2000)
            .easing(TWEEN.Easing.Elastic.Out)
            .delay(6000)
            .onUpdate(() => {
                this.position.y = initialY * (this.scale.x) - 26;
            })

        this.getBackScaleX = new TWEEN.Tween(this.scale)
            .to({ x: this.scale.x }, 2000)
            .easing(TWEEN.Easing.Elastic.Out)
            .delay(6000)
            .onUpdate(() => {
               this.position.y = initialY * (this.scale.x);
            });

        this.doScaleZ = new TWEEN.Tween(this.scale)
            .to({ z: this.scale.z + 1 }, 2000)
            .easing(TWEEN.Easing.Elastic.Out)
            .delay(6000)
            .onUpdate(() => {
                this.position.x = initialX * (this.scale.z) - 2;
            })

        this.getBackScaleZ = new TWEEN.Tween(this.scale)
            .to({ z: this.scale.z }, 2000)
            .easing(TWEEN.Easing.Elastic.Out)
            .delay(6000)
            .onUpdate(() => {
                this.position.x = initialX * (this.scale.z);
             });
            
        this.doScaleX.chain(this.getBackScaleX);
        this.getBackScaleX.chain(this.doScaleZ);
        this.doScaleZ.chain(this.getBackScaleZ);
        this.getBackScaleZ.chain(this.doScaleX);
        this.doScaleX.start();
    }
}