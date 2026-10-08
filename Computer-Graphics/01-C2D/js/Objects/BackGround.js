import {Road} from "./Road.js";
import {Sun} from "./Sun.js";
import {Tree} from "./Tree.js";

import { GraphPoints } from "../Utils/GraphPoints.js";

export class BackGround{
    constructor(){
        this.sun = new Sun();
        this.road = new Road();
        this.tree1 = new Tree({dx: 0.75, dy: 0.24, sx: 0.0007, sy: 0.0007});
        this.tree2 = new Tree({dx: 0.5, dy: 0.32, sx: 0.0005, sy: 0.0005});
        this.tree3 = new Tree({dx: 0.2, dy: 0.32, sx: 0.0006, sy: 0.0006});
    }

    update(){
        this.sun.update();
    }
     
    getGraph(gc){
        let background = {
            transform: {
                dx: 0, 
                dy: 0, 
                sx: 1, 
                sy: 1,
            },    
            style: {
                fill: "skyblue",
                stroke: "skyblue",
                lineWidth: 0.3,
            },    

            shape: GraphPoints.urect(),
            children: [
                this.sun.getGraph(), 
                this.road.getGraph(gc),
                this.getLowerGrassPath(gc),
                this.getUpperGrassPath({dx: 0.3, dy: 0.245, sy: 0.2, sx: 0.2,}),
                this.getUpperGrassPath({dx: -0.2, dy: 0.245, sy: 0.2, sx: 0.2,}),
                this.tree1.getGraph(),
                this.tree2.getGraph(),
                this.tree3.getGraph(),
            ],
        };
        return background;
    }
    getLowerGrassPath(gc){
        const gradient = gc.createLinearGradient(0, 0, 0.96, 0.1);
        gradient.addColorStop(0, " #85A947"); 
        gradient.addColorStop(0.3, " #3E7B27");
        gradient.addColorStop(0.6, " #3E7B27"); 
        gradient.addColorStop(1, " #85A947");
        return {
            transform: {
                dx: 0.0,
                dy: 0.955,
                sy: 0.1,
                sx: 1.0,
            },
            style: {
                fill: gradient,
                lineWidth: 0.1
            },
            shape: GraphPoints.urect(),
            children: [ 

            ]
        };
    }

    getUpperGrassPath(transform){
        return {
            transform: {
                dx: transform.dx,
                dy: transform.dy,
                sy: transform.sy,
                sx: transform.sx,
            },
            style: {
                fill: " #85A947",
                lineWidth: 0.1
            },
            shape: GraphPoints.cubicBezierCurve(
                { x: 1.0, y: 2.0 },
                { x: 1.19, y: 1.5},
                { x: 5.3, y: 1.3 },
                { x: 4.3, y: 2.0 },
                300
            ),
        };
    }

}
