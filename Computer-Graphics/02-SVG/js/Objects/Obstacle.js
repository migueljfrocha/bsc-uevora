import { GraphPoints } from "../Utils/GraphPoints.js";
import { Calc } from "../Utils/Calc.js";

export class Obstacle {
    constructor() {
        let maxVel = Calc.rand_ab(1.5, 2.3);
        this.sx = Calc.rand_ab(1, 2);
        let MIN = Math.floor(this.sx * 20.5);
        let MAX = Math.floor(569 - (this.sx - 1) * 30);
        this.dx = Calc.rand_ab(MIN, MAX);
        this.dy = -10;
        this.sy = this.sx;
        this.speed = maxVel;
        this.isHidden = false;
        this.center = GraphPoints.calculateCenter(this.getObstacleChestPath());

    }

    update(model){
        this.dy += this.speed;
        if(this.dy > 700){
            this.isHidden = true;
        }

       let dist = Calc.dist({x: this.dx, y: this.dy - this.sx * 10}, {x: model.boat.dx, y: model.boat.dy});

        if(dist < (35 * this.sx)){
            model.isOver = true;
        }
    }
    getGraph () {
        const obstaclePath = this.getObstacleChestPath();
        const translatedObstaclePath = GraphPoints.translatePathToCenter(obstaclePath, this.center);
        return {
            transform: {
                dx: this.dx,
                dy: this.dy,
                sx: this.sx,
                sy: this.sy
            },
            style: {
                fill: " #7AB2D3",
                stroke: "black",
                lineWidth: 2
            },
            shape: translatedObstaclePath,
            children: [
                //Face
                this.getObstacleFace(),
                //Vest
                this.getObstacleVestGraph(),
                //Tool
                this.getObstacleToolGraph(),
            ]
        }
    }
    

    getObstacleFace(){
        return {
            transform: {
                dx: 36,
                dy: 30,
                sx: 1,
                sy: 1
            },
            style: {
                fill: " #FCEA2B",
                stroke: "black",
                lineWidth: 2.1
            },
            shape: GraphPoints.translatePathToCenter(GraphPoints.elipse(11, 14, 50), this.center),
            children: [
                //Hair
                this.getObstacleHairGraph(),
                //Eyes
                this.getObstacleEyeGraph({ dx: -4, dy: -2 }), // Left eye
                this.getObstacleEyeGraph({ dx: 4, dy: -2 }),  // Right eye
                //Mouth
                this.getObstacleMouthGraph(),
                //Hat
                this.getObstacleHatGraph()
            ]
        };
    }

    getObstacleChestPath(){
        let obstacleChest = [
            {x: 54.50, y: 58.00},
            {x: 17.00, y: 58.00},
        ];

        obstacleChest = obstacleChest.concat(GraphPoints.quadraticBezierCurve(
            {x: 17.00, y: 58.00},
            {x: 14.93, y: 44.89},
            {x: 26.93, y: 44.89},
            20
        ));

        obstacleChest = obstacleChest.concat(GraphPoints.cubicBezierCurve(
            {x: 26.93, y: 44.89},
            {x: 30.12, y: 47.02},
            {x: 32.86, y: 48.49},
            {x: 35.91, y: 48.48},
            20
        )); 

        obstacleChest = obstacleChest.concat(GraphPoints.cubicBezierCurve(
            {x: 35.91, y: 48.48},
            {x: 39.00, y: 48.49},
            {x: 41.61, y: 47.02},
            {x: 44.81, y: 44.89},
            20
        ));

        obstacleChest = obstacleChest.concat(GraphPoints.cubicBezierCurve(
            {x: 44.81, y: 44.89},
            {x: 48.02, y: 44.89},
            {x: 57.00, y: 46.50},
            {x: 54.50, y: 58.00},
            20
        ));
        return obstacleChest;
    }

    getObstacleEyeGraph(transform){
        return {
            transform: {
                dx: transform.dx,
                dy: transform.dy,
                sx: 1,
                sy: 1
            },
            style: {
                fill: "black",
                stroke: "black",
                lineWidth: 2.3
            },
            shape:GraphPoints.translatePathToCenter(GraphPoints.unit_circle(50), this.center),
        }
    }

    getObstacleMouthGraph(){
        const mouthPath = GraphPoints.sector(50, Math.PI * 0.25, 0.75 * Math.PI).concat(GraphPoints.sector(50, 0.75 * Math.PI, Math.PI * 0.25));
        return {
            transform: {
                dx: -35.5,
                dy: -45,
                sx: 5,
                sy: 4.8
            },
            style: {
                fill: "black",
                stroke: "black",
                lineWidth: 0.4,
                strokeLineCap: "round",
                strokeLineJoin: "round",
            },
            shape:mouthPath,
        }         
    }

    getObstacleToolGraph(){
        return {
            transform: {
                dx: 0,
                dy: 0,
                sx: 1,
                sy: 1
            },
            style: {
                fill: "grey",
                stroke: "black",
                lineWidth: 2,
                strokeLineCap: "round",
                strokeLineJoin: "round",
            },
            shape: GraphPoints.translatePathToCenter(this.getObstacleToolPath(),this.center),
        }
            
    }

    getObstacleToolPath(){
        let getObstacleToolPath = [
            {x: 60.49, y: 44.25},
            {x: 57.06, y: 58.48},
        ]

        getObstacleToolPath = getObstacleToolPath.concat(GraphPoints.cubicBezierCurve(
            {x: 57.06, y: 58.48},
            {x: 56.80, y: 59.57},
            {x: 55.71, y: 60.24},
            {x: 54.62, y: 59.97},
            20
        ));

        getObstacleToolPath = getObstacleToolPath.concat(GraphPoints.cubicBezierCurve(
            {x: 54.62, y: 59.97},
            {x: 53.53, y: 59.71},
            {x: 52.86, y: 58.62},
            {x: 53.13, y: 57.53},
            20
        ));

        getObstacleToolPath = getObstacleToolPath.concat({x: 56.55, y: 43.30});

        getObstacleToolPath = getObstacleToolPath.concat(GraphPoints.cubicBezierCurve(
            {x: 56.55, y: 43.30},
            {x: 55.03, y: 42.14},
            {x: 54.25, y: 40.15},
            {x: 54.72, y: 38.17},
            20
        ));

        getObstacleToolPath = getObstacleToolPath.concat(GraphPoints.cubicBezierCurve(
            {x: 54.72, y: 38.17},
            {x: 55.24, y: 36.02},
            {x: 57.08, y: 34.53},
            {x: 59.17, y: 34.36},
            20
        ));

        getObstacleToolPath = getObstacleToolPath.concat({x: 58.04, y: 38.99}, {x: 60.85, y: 39.66}, {x: 61.98, y: 34.95});

        getObstacleToolPath = getObstacleToolPath.concat(GraphPoints.cubicBezierCurve(
            {x: 61.98, y: 34.95},
            {x: 63.92, y: 36.01},
            {x: 64.99, y: 38.26},
            {x: 64.45, y: 40.51},
            20
        ));

        getObstacleToolPath = getObstacleToolPath.concat(GraphPoints.cubicBezierCurve(
            {x: 64.45, y: 40.51},
            {x: 63.97, y: 42.49},
            {x: 62.37, y: 43.9},
            {x: 60.49, y: 44.25},
            20
        ));

        return getObstacleToolPath;
    }

    getObstacleHairPath(){
        let obstacleHairPath = [
            {x: 47.17, y: 27.23},
            {x: 41.92, y: 20.90},
        ];

        obstacleHairPath = obstacleHairPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 41.92, y: 20.90},
            {x: 40.44, y: 23.71},
            {x: 36.26, y: 23.4},
            20
        ));

        obstacleHairPath = obstacleHairPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 36.26, y: 23.4},
            {x: 36.91, y: 21.1},
            {x: 36.55, y: 19.83},
            20
        ));
        
        obstacleHairPath = obstacleHairPath.concat({x: 24.9, y: 26.36});

        obstacleHairPath = obstacleHairPath.concat(GraphPoints.cubicBezierCurve(
            {x: 24.9, y: 26.36},
            {x: 24.44, y: 26.49},
            {x: 23.92, y: 22.14},
            {x: 25.48, y: 20.47},
            20
        ));

        obstacleHairPath = obstacleHairPath.concat(GraphPoints.cubicBezierCurve(
            {x: 25.48, y: 20.47},
            {x: 26.02, y: 20.35},
            {x: 36.08, y: 15.07},
            {x: 47.04, y: 20.81},
            20
        ));

        obstacleHairPath = obstacleHairPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 47.04, y: 20.81},
            {x: 48.54, y: 26.31},
            {x: 47.17, y: 27.23},
            20
        ));

        return obstacleHairPath;
    }

    getObstacleHairGraph(){
        return {
            transform: {
                dx: -36,
                dy: -30,
                sx: 1,
                sy: 1
            },
            style: {
                fill: " #754E1A",
                stroke: "black",
                lineWidth: 2,
                strokeLineCap: "round",
                strokeLineJoin: "round",
            },
            shape: GraphPoints.translatePathToCenter(this.getObstacleHairPath(), this.center),
        }
    }

    getObstacleHatGraph(){
        return {
            transform: {
                dx: -36,
                dy: -30,
                sx: 1,
                sy: 1
            },
            style: {
                fill: " black",
                stroke: "black",
                lineWidth: 2,
                strokeLineCap: "round",
                strokeLineJoin: "round",
            },
            shape: GraphPoints.translatePathToCenter(this.getObstacleHatCapPath(), this.center),
            children: [
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1
                    },
                    style: {
                        fill: " #7AB2D3",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter(this.getObstacleHatUpperPath(), this.center),
                },

                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1
                    },
                    style: {
                        fill: " white",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter(this.getObstacleHatMiddlePath(), this.center),
                },

            ]
        }
    }

    getObstacleHatCapPath(){
        let obstacleCap = [];

        obstacleCap = obstacleCap.concat(GraphPoints.quadraticBezierCurve(
            {x: 22.42, y: 22.41},
            {x: 23.39, y: 21.58},
            {x: 25.78, y: 20.27},
            20
        ));

        obstacleCap = obstacleCap.concat(GraphPoints.cubicBezierCurve(
            {x: 25.78, y: 20.27},
            {x: 32.84, y: 16.92},
            {x: 43.62, y: 17.43},
            {x: 49.81, y: 22.41},
            20
        ));

        obstacleCap = obstacleCap.concat(GraphPoints.cubicBezierCurve(
            {x: 49.81, y: 22.41},
            {x: 43.62, y: 17.43},
            {x: 32.84, y: 16.92},
            {x: 25.78, y: 20.27},
            20
        ));

        obstacleCap = obstacleCap.concat(GraphPoints.quadraticBezierCurve(
            {x: 25.78, y: 20.27},
            {x: 23.39, y: 21.58},
            {x: 22.42, y: 22.41},
            20
        ));
        return obstacleCap;

    }


    getObstacleHatUpperPath(){
        let obstacleHatUpper = [];

        obstacleHatUpper = obstacleHatUpper.concat(GraphPoints.quadraticBezierCurve(
            {x: 46.98, y: 20.54},
            {x: 48.23, y: 9.26},
            {x: 36.93, y: 9.23},
            20
        ));
        obstacleHatUpper = obstacleHatUpper.concat({x: 35.13, y: 9.23});

        obstacleHatUpper = obstacleHatUpper.concat(GraphPoints.quadraticBezierCurve(
            {x: 35.13, y: 9.23},
            {x: 23.83, y: 9.26},
            {x: 25.08, y: 20.54},
            20
        ));

        obstacleHatUpper = obstacleHatUpper.concat(GraphPoints.cubicBezierCurve(
            {x: 25.08, y: 20.54},
            {x: 33, y: 17.5},
            {x: 39.5, y: 17},
            {x: 46.98, y: 20.54},
            20
        ));

        return obstacleHatUpper;
    }

    getObstacleHatMiddlePath(){

        let obstacleHatMiddle = [
            {x: 39.12, y: 15.94},
            {x: 39.12, y: 18.26},
            {x: 33.12, y: 18.26},
            {x: 33.12, y: 15.94},
        ];

        obstacleHatMiddle = obstacleHatMiddle.concat(GraphPoints.cubicBezierCurve(
            {x: 33.12, y: 15.94},
            {x: 33.12, y: 14.29},
            {x: 34.46, y: 12.94},
            {x: 36.12, y: 12.94},
            20
        ));

        obstacleHatMiddle = obstacleHatMiddle.concat(GraphPoints.cubicBezierCurve(
            {x: 36.12, y: 12.94},
            {x: 37.77, y: 12.92},
            {x: 39.12, y: 14.29},
            {x: 39.12, y: 15.94},
            20
        ));
        return obstacleHatMiddle;
    }

    getObstacleVestPath(){
        let obstacleVest = [
            {x: 49.3, y: 46.00},
            {x: 49.3, y: 57.87},
            {x: 22.26, y: 57.87},
            {x: 22.26, y: 45.76},
            {x: 26.93, y: 44.89},
        ];

        obstacleVest = obstacleVest.concat(
            {x: 26.93, y: 51.50},
            {x: 44.81, y: 51.50},
            {x: 44.81, y: 51.50},
            {x: 44.81, y: 44.89}
        );

        return obstacleVest;
    }

    getObstacleVestGraph(){
        return{
            transform: {
                dx: 0,
                dy: 0,
                sx: 1,
                sy: 1
            },
            style: {
                fill: " #4A628A",
                stroke: "black",
                lineWidth: 2,
                strokeLineCap: "round",
                strokeLineJoin: "round",
            },
            shape: GraphPoints.translatePathToCenter(this.getObstacleVestPath(), this.center),

            children : [
                {
                    transform: {
                        dx: 0,
                        dy: 0,
                        sx: 1,
                        sy: 1
                    },
                    style: {
                        fill: "black",
                        stroke: "black",
                        lineWidth: 2,
                        strokeLineCap: "round",
                        strokeLineJoin: "round",
                    },
                    shape: GraphPoints.translatePathToCenter([{x: 27, y: 55}, {x: 32.68, y: 55}], this.center),
                }
            ]
        }
    }

}