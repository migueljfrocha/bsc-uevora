import { GraphPoints } from '../Utils/GraphPoints.js';
import { Calc } from '../Utils/Calc.js';
export class Bird{
    constructor(){
        let randomY = Calc.rand_ab(0.27, 0.4);
        let randomSize = Calc.rand_ab(0.00005, 0.00016);

        this.dx = 1.1;         
        this.dy = randomY;
        this.sx = randomSize;
        this.sy = randomSize;
        this.isHidden = false;
    }

    getGraph(){
        return {
            transform: {
                dx: this.dx,
                dy: this.dy,
                sx: this.sx,
                sy: this.sy,
            },
            style: {
                fill: " #B59F78",
                stroke: "black",
                lineWidth: 0.1
            },
            shape: this.getBirdsPath(),
        };
    }

    update(dt){
        this.dx -= 0.00006 * dt;
        if(this.dx < -0.1){
            this.isHidden = true;
        }

    }
    getBirdsPath(){
        let birdsPath = [];
        birdsPath = birdsPath.concat(GraphPoints.cubicBezierCurve(
            {x: 130, y: 667.85},
            {x: 231.44, y: 622.11},
            {x: 369.93, y: 916.14},
            {x: 630, y: 556.77},
            300
        ));

        birdsPath = birdsPath.concat(GraphPoints.cubicBezierCurve(
            {x: 630, y: 556.77},
            {x: 571.73, y: 583.93},
            {x: 548.35, y: 587.72},
            {x: 484.68, y: 590.81},
            300
        ));

        birdsPath = birdsPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 484.68, y: 590.81},
            {x: 506.26, y: 564.33},
            {x: 496.91, y: 521},
            300
        ));

        birdsPath = birdsPath.concat(GraphPoints.cubicBezierCurve(
            {x: 496.91, y: 521},
            {x: 391.87, y: 523.75},
            {x: 297.99, y: 530.97},
            {x: 291.51, y: 590.81},
            300
        ));

        birdsPath = birdsPath.concat(GraphPoints.cubicBezierCurve(
            {x: 291.51, y: 590.81},
            {x: 163.09, y: 556.42},
            {x: 193.07, y: 637.76},
            {x: 130, y: 667.85},
            300
        ));

        return birdsPath;
    }
}