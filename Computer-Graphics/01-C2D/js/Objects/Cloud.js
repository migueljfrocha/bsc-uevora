import { GraphPoints } from "../Utils/GraphPoints.js";
import { Calc } from "../Utils/Calc.js"; 

export class Cloud{

    constructor(){
        let randomY = Calc.rand_ab(0.05, 0.25);
        let randomSize = Calc.rand_ab(0.00025, 0.0004);
        let angleRandom = Calc.rand_ab(0, -Math.PI/6); 
        this.dx = -0.4;         
        this.dy = randomY;
        this.sx = randomSize;
        this.sy = randomSize;
        this.a = angleRandom;
        this.isHidden = false;  
    }                                       
    getGraph(){
        return {
            transform: {
                dx: this.dx,
                dy: this.dy,
                sx: this.sx,
                sy: this.sy,
                a: this.a,
            },
            style: {
                fill: "rgba(251, 251, 251, 0.5)",
                stroke: "black",
                lineWidth: 0.003
            },
            shape: this.getCloudPath(),
        };
    };

    update(dt){
        this.dx += 0.000023 * dt;
        if(this.dx > 1.1){
            this.isHidden = true;
        }

    }

    getCloudPath(){
        let cloudPath = [];

        cloudPath = cloudPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 436.71, y: 429.9 },
            { x: 217.71, y: 243.9},
            { x: 501.21, y: 299.4 },
            300
        ));

        cloudPath = cloudPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 501.21, y: 299.4 },
            { x: 683.21, y: 54.9},
            { x: 675.21, y: 312.4},
            300
        ));

        cloudPath = cloudPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 675.21, y: 312.4},
            { x: 953, y: 313},
            { x: 692, y: 432 },
            300
        ));

        cloudPath = cloudPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 692, y: 432 },            
            { x: 598.5, y: 639.5 },
            { x: 540, y: 496.5 },
            300
        ));
        cloudPath = cloudPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 540, y: 496.5 },            
            { x: 154.5, y: 561.5 },
            { x: 436.71, y: 429.9 },
            300
        )); 
        return cloudPath;
    };
}