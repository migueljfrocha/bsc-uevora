import { GraphPoints } from "../Utils/GraphPoints.js";
export class Banner{
    constructor(gc){
        this.gradient = gc.createLinearGradient(234, 0, 725, 0);
        this.gradient.addColorStop(0, " #F72C5B"); 
        this.gradient.addColorStop(0.3, " #FF748B");
        this.gradient.addColorStop(0.6, " #FF748B"); 
        this.gradient.addColorStop(1, " #F72C5B");

        this.speed = 0.003;
        this.x = 0.1;
        this.y = 0.1;
        this.transform ={
            dx: -0.4,
            dy: 3,
            sx: 0.0005,
            sy: 0.0005,
        }
    }
    update(model){
        this.transform.dx += model.plane.transform.dx;
        this.transform.dy += model.plane.transform.dy;
    }
    getGraph(){

        return {
            transform: this.transform,
            style: {
                fill: this.gradient,
                stroke: "black",
                lineWidth: 0.2
            },
            shape: this.getBannerPath(),
        };
    }

    getBannerPath(){
        let bannerPath = [{x:234, y:67}, {x:234, y: 202}];

        bannerPath = bannerPath.concat(GraphPoints.cubicBezierCurve(
            {x: 234, y: 202},
            {x: 425.75, y: 202},
            {x: 497, y: 244.5},
            {x: 725, y: 202},
            300
        ));
        bannerPath = bannerPath.concat({x: 725, y: 202}, {x:725, y: 67});
        
        bannerPath = bannerPath.concat(GraphPoints.cubicBezierCurve(
            {x:725, y: 67},
            {x:633.5, y: 117},
            {x:278, y: 27.5},
            {x:234, y:67},
            300
        ));
        return bannerPath;
    }
}