import { GraphPoints } from '../Utils/GraphPoints.js';
import { ContextRender } from '../Render/ContextRender.js';
export class LetterN {

    getLetterPath(){
        let NPath = [ {x: 62.62, y: 51.66}, 
                      {x: 62.62, y: 55.94}, 
                      {x: 63.38, y: 56.06}, 
                      {x: 63.38, y: 56.29}, 
                      {x: 61.09, y: 56.29}, 
                      {x: 61.09, y: 56.06},
                      {x: 61.76, y: 55.94},
                      {x: 61.76, y: 51.28},
                      {x: 60.96, y: 51.14},
                      {x: 60.96, y: 50.88},
                      {x: 62.56, y: 50.88},
                      {x: 62.62, y: 51.36}
        ];

        NPath = NPath.concat(GraphPoints.cubicBezierCurve(
            {x: 62.62, y: 51.36},
            {x: 65.39, y: 49.78},
            {x: 65.87, y: 51.14},
            {x: 65.92, y: 51.93},
            50
        ));

        NPath = NPath.concat(
            {x: 65.95, y: 55.94},
            {x: 66.62, y: 56.06},
            {x: 66.62, y: 56.29},
            {x: 64.17, y: 56.29},
            {x: 64.17, y: 56.06},
            {x: 64.99, y: 55.94},
            {x: 64.99, y: 52.38}
        );

        NPath = NPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 64.99, y: 52.38},
            {x: 65.09, y: 50.88},
            {x: 62.62, y: 51.66},
            50
        ));

        return NPath;
    }

    getGeometry(){
        let NPath = this.getLetterPath();
        let holeNpath = null;
        return ContextRender.getExtrusion(NPath, holeNpath, 0.6);
    }
}