import { GraphPoints } from "../Utils/GraphPoints.js";
import { Calc } from "../Utils/Calc.js";

export class Boat {

    constructor(inputVel) {
        let randomX = Calc.rand_ab(30, 570);

        this.action = 0;
        this.dy = 530;    
        this.dx = randomX;
        this.sx = 2;
        this.sy = 2;
        this.v = inputVel;
    }
    
    update(){
        this.dx = Calc.clip(this.dx, 50 , 550);

        if(this.action === 1){
            this.dx += -this.v;
        }else if(this.action === 2){
            this.dx += this.v;
        }
    }
    getFinalGraph() {
        switch (this.action) {
            case 1:
                this.sx = -2;
                return this.getGraph();
            case 2:
                this.sx = 2;
                return this.getGraph();
            case 0:
                return this.getFrontGraph();
            default:
                return this.getFrontGraph();
        }
    }
    //
    // SIDE BOAT
    //
    getGraph() {
        const boatBodyPath = this.getBoatBodyPath();
        const center = GraphPoints.calculateCenter(boatBodyPath);
        const translatedBoatBodyPath = GraphPoints.translatePathToCenter(boatBodyPath, center);
        return{
            transform: {
                dx: this.dx,
                dy: this.dy, 
                sx: this.sx, 
                sy: this.sy,
            },
            style: {
                fill: "rgb(255, 127, 22)",
                stroke: "black",
                lineWidth: 2,
                strokeLineCap: "round",
                strokeLineJoin: "round",
            },
            shape: translatedBoatBodyPath,
            children: [
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1,
                        a: 0,
                    },
                    style: {
                        fill: "rgb(249, 56, 39)",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter(this.getBoatMotorPath(), center),
                },
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1,
                        a: 0,
                    },
                    style: {
                        fill: "rgb(212, 235, 248)",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter(this.getBoatWindShieldPath(), center),
                }
            ]
        }
    }

    getBoatBodyPath(){
        let boatPath = [
            {x: 483.56, y: 222.80},
            {x: 485.64, y: 229.03},
            {x: 522.06, y: 216.39},
            {x: 524.86, y: 210.19},
            {x: 525.77, y: 206.27},
        ];

        boatPath = boatPath.concat(GraphPoints.cubicBezierCurve(
            {x: 525.77, y: 206.27}, 
            {x: 525.90, y: 205.49}, 
            {x: 525.10, y: 204.90},
            {x: 524.34, y: 205.22},
            10
        ));

        boatPath = boatPath.concat({x: 484.2, y: 221.55});

        boatPath = boatPath.concat(GraphPoints.cubicBezierCurve(
            {x: 484.20, y: 221.55}, 
            {x: 483.68, y: 221.75}, 
            {x: 483.40, y: 222.31},
            {x: 483.56, y: 222.80},
            10
        ));

        return boatPath;
    }

    getBoatMotorPath(){
        let motorPath = [
            {x: 483.09, y: 222.96},
            {x: 483.22, y: 223.37},
            {x: 480.02, y: 224.43},
        ];

        motorPath = motorPath.concat(GraphPoints.cubicBezierCurve(
            {x: 480.02, y: 224.43}, 
            {x: 479.44, y: 224.63}, 
            {x: 478.81, y: 224.36},
            {x: 478.64, y: 223.84},
            10
        ));

        motorPath = motorPath.concat({x: 477.24, y: 219.63});

        motorPath = motorPath.concat(GraphPoints.cubicBezierCurve(
            {x: 477.24, y: 219.63},
            {x: 477.07, y: 219.11},
            {x: 477.41, y: 218.53},
            {x: 477.99, y: 218.33},
            10
        ));

        motorPath = motorPath.concat({x: 484.40, y: 216.20});

        motorPath = motorPath.concat(GraphPoints.cubicBezierCurve(
            {x: 484.40, y: 216.20},
            {x: 484.98, y: 216.00},
            {x: 485.61, y: 216.27},
            {x: 485.78, y: 216.79},
            10
        ));


        motorPath = motorPath.concat({x: 486.98, y: 220.41});

        motorPath = motorPath.concat(GraphPoints.cubicBezierCurve(
            {x: 486.98, y: 220.41},
            {x: 483.26, y: 221.89},
            {x: 482.92, y: 222.46},
            {x: 483.09, y: 222.96},
            10
        ));

        return motorPath;
    }

    getBoatWindShieldPath(){
        let windShieldPath = [
            {x: 517.40, y: 208.02},     
            {x: 502.77, y: 214.00}, 
            {x: 501.01, y: 208.73}
        ];

        windShieldPath = windShieldPath.concat(GraphPoints.cubicBezierCurve(
            {x: 501.01, y: 208.73},
            {x: 500.85, y: 208.25}, 
            {x: 501.08, y: 207.71},
            {x: 501.55, y: 207.50},
            10
        ));

        windShieldPath = windShieldPath.concat(GraphPoints.cubicBezierCurve(
            {x: 501.55, y: 207.50}, 
            {x: 509.80, y: 203.90}, 
            {x: 515.86, y: 207.07},
            {x: 517.40, y: 208.02},
            10
        ));
        return windShieldPath;
    }

    //
    //FRONT SIDED BOAT
    //

    getFrontGraph() {
        const boatChassisPath = this.getFrontBoatChassisPath();
        const center = GraphPoints.calculateCenter(boatChassisPath);
        const translatedBoatChassisPath = GraphPoints.translatePathToCenter(boatChassisPath, center);
        return {
            transform: {
                dx: this.dx,
                dy: this.dy,  
                sx: this.sx, 
                sy: this.sy,  
            },
            style: {
                fill: "rgb(255, 127, 22)",
                stroke: "black",
                lineWidth: 2,
                strokeLineCap: "round",
                strokeLineJoin: "round",
            },
            shape: translatedBoatChassisPath,
            children: [
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1,
                        a: 0,
                    },
                    style: {
                        fill: "rgb(249, 56, 39)",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter(this.getFrontBoatMotorPath(), center),
                },
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1,
                        a: 0,
                    },
                    style: {
                        fill: "rgb(212, 235, 248)",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter(this.getFrontBoatWindShieldPath(), center),
                },
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1,
                        a: 0,
                    },
                    style: {
                        fill: "white",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter([{x: 431.50, y: 229.50},{x: 427.00, y: 229.50}], center),
                }
            ]
        }
    }


    getFrontBoatChassisPath(){
        let boatChassisPath = [
            {x: 421.00, y: 215.00},
            {x: 422.00, y: 233.00},
            {x: 436.00, y: 233.00},
            {x: 437.00, y: 215.00}
        ];

        boatChassisPath = boatChassisPath.concat(GraphPoints.cubicBezierCurve(
            {x: 437.00, y: 215.00},
            {x: 437.50, y: 209.00},
            {x: 435.00, y: 202.00},
            {x: 429.00, y: 196.00},
            10
        ));

        boatChassisPath = boatChassisPath.concat(GraphPoints.cubicBezierCurve(
            {x: 429.00, y: 196.00},
            {x: 423.00, y: 202.00},
            {x: 420.50, y: 209.00},
            {x: 421.00, y: 215.00},
            10
        ));

        return boatChassisPath;
    }

    getFrontBoatMotorPath(){

        let motorPath = [
            {x: 431.50, y: 233.00},
            {x: 431.50, y: 236.13},
            {x: 427.00, y: 236.13},
            {x: 427.00, y: 233.00}
        ];

        return motorPath;
    }

    getFrontBoatWindShieldPath(){
        let windShieldPath = [];

        windShieldPath = windShieldPath.concat(GraphPoints.cubicBezierCurve(
            {x: 436.50, y: 210.00},
            {x: 430.50, y: 204.00},
            {x: 427.50, y: 204.00},
            {x: 421.50, y: 210.00},
            10
        ));

        return windShieldPath;
    }

}