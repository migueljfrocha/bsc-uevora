import { GraphPoints } from "../Utils/GraphPoints.js";

export class Sun {

    constructor() {
        this.radius = 2;
        this.color = " #FD841F ";
        this.angle = Math.PI / 2;
        this.angleStep = Math.PI /2000;
    }

    getGraph() {
        return {
            transform: {
                dx: 0.9,
                dy: 0.1,
                sx: 0.025,
                sy: 0.025,
                a: this.angle,
            },
            style: {
                fill: this.color,
                stroke: " #EB5B00",
                lineWidth: 0.1,
            },
            shape: GraphPoints.star(11, this.radius),
        };
    }

    update() {
        this.angle += this.angleStep;
    }
}