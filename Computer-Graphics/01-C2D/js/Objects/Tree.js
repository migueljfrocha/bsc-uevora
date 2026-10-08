import { GraphPoints } from "../Utils/GraphPoints.js";
export class Tree{
    
    constructor(transform){
        this.dx = transform.dx;
        this.dy = transform.dy;
        this.sx = transform.sx;
        this.sy = transform.sy;
    };

    getGraph(){
        return {
            transform: {
                dx: this.dx,
                dy: this.dy,
                sx: this.sx,
                sy: this.sy,
                
            },
            style: {
                fill: " #543A14",
                stroke: "black",
                lineWidth: 0.002
            },
            shape: this.getTrunkPath(),
            children: [
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1,
                    },
                    style: {
                        fill: " #A7D477",
                        stroke: " #A7D477",
                        lineWidth: 0.002
                    },
                    shape: this.getLeavesPath(),
                }
            ]
        };
    }

    getTrunkPath(){
        let trunkPath = [{x: 221.85, y: 394.54}, {x: 211.14, y: 394.54}, {x: 194.41, y: 418.57}];

        trunkPath = trunkPath.concat(GraphPoints.quadraticBezierCurve(
                {x: 194.41, y: 418.57}, 
                {x: 210.78, y: 456.09}, 
                {x: 183.64, y: 515.95}, 
                15
        ));

        trunkPath = trunkPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 183.64, y: 515.95}, 
            {x: 215.85, y: 505.36},
            {x: 248.65, y: 515.95}, 
            15
        ));

        trunkPath = trunkPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 248.65, y: 515.95}, 
            {x: 219.53, y: 456.09},
            {x: 248.65, y: 390.25}, 
            15
        ));

        trunkPath = trunkPath.concat({x: 248.65, y: 390.25}, {x: 242.48, y: 385.76});

        trunkPath = trunkPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 242.48, y: 385.76},
            {x: 227.75, y: 405.51},
            {x: 222.67, y: 435.83}, 
            15
        ));

        trunkPath = trunkPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 222.67, y: 435.83},
            {x: 215.43, y: 405.78},
            {x: 221.85, y: 394.54}, 
            15
        ));

        return trunkPath;
    }

    getLeavesPath(){
        let leavesPath = [];

        leavesPath = leavesPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 155.99, y: 409.53},
            {x: 143.14, y: 451.30},
            {x: 265.77, y: 403.37},
            15
        ));

        leavesPath = leavesPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 265.77, y: 403.37},
            {x: 294.95, y: 388.64},
            {x: 257.59, y: 369.50},
            15
        ));

        leavesPath = leavesPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 257.59, y: 369.50},
            {x: 279.15, y: 322.51},
            {x: 242.48, y: 338.84},
            15
        ));

        leavesPath = leavesPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 242.48, y: 338.84},
            {x: 226.14, y: 317.97},
            {x: 202.04, y: 320.37},
            15
        ));

        leavesPath = leavesPath.concat(GraphPoints.cubicBezierCurve(
            {x: 202.04, y: 320.37},
            {x: 169.83, y: 323.57},
            {x: 129.74, y: 352.31},
            {x: 153.94, y: 373.88},
            15
        ));

        leavesPath = leavesPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 153.94, y: 373.88},
            {x: 100.30, y: 394.54},
            {x: 155.99, y: 409.53},
            15
        ));

        return leavesPath;
    }
    }

