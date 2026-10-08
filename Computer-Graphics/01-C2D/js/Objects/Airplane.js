import { GraphPoints } from "../Utils/GraphPoints.js";
export class Plane{
    constructor(){
        this.transform = {
            dx: -0.5,
            dy: 0.3,
            sx: 0.0008,
            sy: 0.0008,
        }
    }

    getGraph(){
        return {
            //BODY
            transform: this.transform,
            style: {
                fill: " #B7B7B7",
                stroke: " #B7B7B7",
                lineWidth: 0.1
            },
            shape: this.getPlaneBodyPath(),
            children: [

                //WINGS

                {
                    transform: {
                        dx: 0.0,
                        dy: 0.0,
                        sx: 1.0,
                        sy: 1.0,
                    },
                    style: {
                        fill: " #074799",
                        stroke: " #074799",
                        lineWidth: 0.1
                    },
                    shape: this.getPlaneFrontWingPath()
                },
                {
                    transform: {
                        dx: 0.0,
                        dy: 0.0,
                        sx: 1.0,
                        sy: 1.0,
                    },
                    style: {
                        fill: " #074799",
                        stroke: " #074799",
                        lineWidth: 0.1
                    },
                    shape: this.getPlaneBackWingPath()
                },

                {
                    transform: {
                        dx: 120,
                        dy: 100,
                        sx: 25,
                        sy: 25,
                    },
                    style: {
                        fill: " #001A6E ",
                        stroke: " #001A6E",
                        lineWidth: 0.1
                    },
                    shape: this.getPlaneUpperWingPath()
                },

                //WINDOWS
                this.getWindowGraph({dx: 190, dy: 250, sx: 10, sy: 12}),
                this.getWindowGraph({dx: 230, dy: 250, sx: 10, sy: 12}),
                this.getWindowGraph({dx: 270, dy: 250, sx: 10, sy: 12}),
                this.getWindowGraph({dx: 310, dy: 255, sx: 10, sy: 12}),
                this.getWindowGraph({dx: 350, dy: 255, sx: 10, sy: 12}),
                this.getWindowGraph({dx: 390, dy: 255, sx: 10, sy: 12}),
                this.getWindowGraph({dx: 430, dy: 255, sx: 10, sy: 12}),
                
                {
                    transform: {
                        dx: 0.0,
                        dy: 0.0,
                        sx: 1.0,
                        sy: 1.0,
                    },
                    style: {
                        fill: " #001A6E ",
                        stroke: " #001A6E",
                        lineWidth: 0.1
                    },
                    shape: this.getPlaneDriverWindow()
                },

                //WHEELS
                this.getPlaneWheelsGraph({dx: 500, dy: 325, sx: 12, sy: 12}),
                this.getPlaneWheelsGraph({dx: 450, dy: 325, sx: 12, sy: 12}),
                this.getPlaneWheelsGraph({dx: 400, dy: 325, sx: 12, sy: 12})
            ]
        };
    }

    getPlaneBodyPath(){
        let bodyPath = [
            {x: 435, y: 216.5},
            {x: 201.98, y: 213.67},
        ];

        bodyPath = bodyPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 201.98, y: 213.67},
            { x: 154.84, y: 218.86 },
            { x: 112.5, y: 231.5 },
            300
        ));

        bodyPath = bodyPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 112.5, y: 231.5 },
            { x: 106.14, y: 235.85 },
            { x: 107.83, y: 239.32 },
            300
        ));

        bodyPath = bodyPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 107.83, y: 239.32 },
            { x: 109.51, y: 242.79 },
            { x: 174.23, y: 278.6 },
            300
        ));

        bodyPath = bodyPath.concat(GraphPoints.cubicBezierCurve(
            { x: 174.23, y: 278.6 },
            { x: 118.09, y: 258.11 },
            { x: 230.38, y: 299.09 },
            { x: 333.51, y: 310.57 },
            300
        ));

        bodyPath = bodyPath.concat({ x: 498.12, y: 313.95 });

        bodyPath = bodyPath.concat(GraphPoints.cubicBezierCurve(
            { x: 498.12, y: 313.95 },
            { x: 558.81, y: 310.25 },
            { x: 612.17, y: 302.56 },
            { x: 611.14, y: 284.61 },
            300
        ));

        bodyPath = bodyPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 611.14, y: 284.61 },
            { x: 610.1, y: 266.66 },
            { x: 574.83, y: 254.51 },
            300
        ));

        bodyPath = bodyPath.concat({ x: 569.16, y: 248.34 });

        bodyPath = bodyPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 569.16, y: 248.34 },
            { x: 567.65, y: 239.51 },
            { x: 552.43, y: 231.13 },
            300
        ));

        bodyPath = bodyPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 552.43, y: 231.13 },
            { x: 537.2, y: 222.74 },
            {x: 435, y: 216.5},
            300
        ));


        return bodyPath;
    }

    getPlaneDriverWindow(){
        let driverWindow = [
            {x: 578.9, y: 259.71},
            {x: 569.13, y: 250.85},
        ];

        driverWindow = driverWindow.concat(GraphPoints.quadraticBezierCurve(
            { x: 569.13, y: 250.85},
            { x: 564.59, y: 256.77 },
            { x: 554.57, y: 259.51 },
            300
        ));
        
        driverWindow = driverWindow.concat(GraphPoints.cubicBezierCurve(
            { x: 554.57, y: 259.51 },
            { x: 544.5, y: 262.21 },
            { x: 535.97, y: 263.05 },
            { x: 531.44, y: 263.52 },
            300
        ));

        driverWindow = driverWindow.concat(GraphPoints.cubicBezierCurve(
            { x: 531.44, y: 263.52 },
            { x: 526.91, y: 263.98 },
            { x: 523.47, y: 266.4 },
            { x: 524, y: 269.98 },
            300
        ));

        driverWindow = driverWindow.concat(GraphPoints.cubicBezierCurve(
            { x: 524, y: 269.98 },
            { x: 524.54, y: 273.36 },
            { x: 526.74, y: 276.12 },
            { x: 533.16, y: 276.34 },
            300
        ));



        driverWindow = driverWindow.concat(GraphPoints.quadraticBezierCurve(
            { x: 533.16, y: 276.34 },
            { x: 555.49, y: 275.78 },
            { x: 567.42, y: 270.39 },
            300
        ));

        driverWindow = driverWindow.concat(GraphPoints.quadraticBezierCurve(
            { x: 567.42, y: 270.39 },
            { x: 579.36, y: 265 },
            { x: 578.9, y: 259.71 },
            300
        ));


        return driverWindow;
    }

    getPlaneFrontWingPath(){
        let frontWingPath = [
            { x: 381.63, y: 293.05}, 
            { x: 290.21, y: 287.66}, 
            { x: 217.4, y: 320.52}, 
            { x: 128.67, y: 341.6}, 
            { x: 122.74, y: 327.51}, 
            { x: 109.28, y: 328.84}, 
            { x: 118.8, y: 363.65}
        ];

        frontWingPath = frontWingPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 118.8, y: 363.65},
            { x: 130.98, y: 370.65},
            { x: 264.23, y: 338.65},
            300
        ));

        frontWingPath = frontWingPath.concat(GraphPoints.cubicBezierCurve(
            { x: 264.23, y: 338.65},
            { x: 222.11, y: 348.77},
            { x: 306.34, y: 328.53},
            { x: 264.23, y: 338.65},
            300
        ));

        frontWingPath = frontWingPath.concat(GraphPoints.cubicBezierCurve(
            { x: 264.23, y: 338.65},
            { x: 358.65, y: 312.32},
            { x: 387.85, y: 303.16},
            { x: 373.25, y: 307.74},
            300
        ));

        frontWingPath = frontWingPath.concat(GraphPoints.cubicBezierCurve(
            { x: 373.25, y: 307.74},
            { x: 385.32, y: 294.27},
            { x: 377.94, y: 291.83},
            { x: 381.63, y: 293.05},
            300
        ));

        return frontWingPath;
    };

    getPlaneBackWingPath(){
        let backWingPath = [
            {x: 165.75, y: 248.4},
            {x: 146.33, y: 242},
            {x: 43.87, y: 253.2}
        ];

        backWingPath = backWingPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 43.87, y: 253.2},
            {x: 34.73, y: 256.67},
            {x: 35.02, y: 259.6},
            300
        ));

        backWingPath = backWingPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 35.02, y: 259.6},
            {x: 35.31, y: 262.52},
            {x: 51.6, y: 264.39},
            300
        ));

        backWingPath = backWingPath.concat({x: 168.34, y: 260.65});

        backWingPath = backWingPath.concat(GraphPoints.cubicBezierCurve(
            {x: 168.34, y: 260.65},
            {x: 164.92, y: 260.92},
            {x: 171.76, y: 260.37},
            {x: 165.75, y: 248.4},
            300
        ));

        return backWingPath
    };

    getPlaneWheelsGraph(transform){
        return{
            transform: {
                dx: transform.dx,
                dy: transform.dy,
                sx: transform.sx,
                sy: transform.sy,
            },
            style: {
                fill: "black",
                stroke: "white",
                lineWidth: 0.1
            },
            shape: GraphPoints.unit_circle(30)
        };
    };

    getPlaneWindowsPath(){
        let windowsPath = [];
        windowsPath = windowsPath.concat(GraphPoints.unit_circle(100));

        return windowsPath;
    };

    getPlaneUpperWingPath(){
        let upperWingPath = [];
        upperWingPath = upperWingPath.concat(
            { x: 3.5, y: 5},
            { x: 1.0, y: 5},
            { x: 0.48, y: 2.18},
            { x: 1.50, y: 2},
        );
        upperWingPath = upperWingPath.concat(GraphPoints.quadraticBezierCurve(
            { x: 1.50, y: 2},
            { x: 1.58, y: 3.45},
            { x: 3.5, y: 5},
            300
        ));
        return upperWingPath;
    };

    getWindowGraph(transform){
        return{
            transform: {
                dx: transform.dx,
                dy: transform.dy,
                sx: transform.sx,
                sy: transform.sy,
            },
            style: {
                fill: " #001F3F",
                stroke: " #001F3F",
                lineWidth: 0.1
            },
            shape: this.getPlaneWindowsPath()
        };
    };

}