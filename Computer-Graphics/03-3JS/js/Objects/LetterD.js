import { GraphPoints } from '../Utils/GraphPoints.js';
import { ContextRender } from '../Render/ContextRender.js';
export class LetterD {

    getLetterPath(){
        let DPath = [ {x: 52.84, y: 50.75}, 
                      {x: 52.84, y: 48.47}, 
                      {x: 51.97, y: 48.34}, 
                      {x: 51.97, y: 48.04}, 
                      {x: 53.78, y: 48.04}, 
                      {x: 53.78, y: 55.93},
                      {x: 54.36, y: 56.02},
                      {x: 54.36, y: 56.28},
                      {x: 53.00, y: 56.28},
                      {x: 52.84, y: 55.93},
        ];

        DPath = DPath.concat(GraphPoints.cubicBezierCurve(
            {x: 52.84, y: 55.93},
            {x: 50.79, y: 57.41},
            {x: 49.05, y: 55.94},
            {x: 49.16, y: 53.34},
            50
        ));

        DPath = DPath.concat(GraphPoints.cubicBezierCurve(
            {x: 49.16, y: 53.34},
            {x: 49.26, y: 51.20},
            {x: 50.57, y: 50.48},
            {x: 52.84, y: 50.75},
            50
        ));


        return DPath;
    }
    getLetterHolePath(){
        let holeDpath = [ {x: 52.84, y: 51.21}, 
                          {x: 52.84, y: 55.65}
                        ];
        holeDpath = holeDpath.concat(GraphPoints.quadraticBezierCurve(
            {x: 52.84, y: 55.65},
            {x: 49.63, y: 57.17},
            {x: 50.16, y: 53.07},
            50
        ));

        holeDpath = holeDpath.concat(GraphPoints.cubicBezierCurve(
            {x: 50.16, y: 53.07},
            {x: 50.30, y: 51.78},
            {x: 51.29, y: 50.85},
            {x: 52.84, y: 51.21},
            50
        ));
        return holeDpath;

    }
    getGeometry(){
        let DPath = this.getLetterPath();
        let holeDpath = this.getLetterHolePath();
        return ContextRender.getExtrusion(DPath, holeDpath, 0.6);
    }
}