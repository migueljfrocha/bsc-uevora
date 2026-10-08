import { GraphPoints } from "../Utils/GraphPoints.js";

export class Road{
    getGraph(gc){
        const gradient = gc.createLinearGradient(0,0,1.25,0);
        gradient.addColorStop(0, "black");
        gradient.addColorStop(0.3, "gray");
        gradient.addColorStop(0.6, "black");
        gradient.addColorStop(1, "gray");

        return {
            transform: {
                dx: 0.0,
                dy: 0.75,
                sy: 0.1,
                sx: 1.2,
            },
            style: {
                fill: gradient,
                stroke: "black",
                lineWidth: 0.1
            },
            shape: GraphPoints.urect(),
            children: [
                this.getRoadLines({x: 0.0, y: 0.0, sy: 0.1, sx: 0.1}),
                this.getRoadLines({x: 0.3, y: 0.0, sy: 0.1, sx: 0.1}),
                this.getRoadLines({x: 0.6, y: 0.0, sy: 0.1, sx: 0.1}),
                this.getRoadLines({x: 0.9, y: 0.0, sy: 0.1, sx: 0.1}),
                this.getRoadLines({x: 0.0, y: -0.75, sy: 0.1, sx: 1.0}),
                this.getRoadLines({x: 0.0, y: 0.75, sy: 0.1, sx: 1.0}),

            ]
        };
    }

    getRoadLines(transform){
        return {
            transform: {
                dx: transform.x,
                dy: transform.y,
                sy: transform.sy,
                sx: transform.sx,
            },
            style: {
                fill: "white",
                stroke: "white",
                lineWidth: 0.01
            },
            shape: GraphPoints.urect(),
        };
    }
}
