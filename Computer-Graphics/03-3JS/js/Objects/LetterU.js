import { GraphPoints } from '../Utils/GraphPoints.js';
import { ContextRender } from '../Render/ContextRender.js';
export class LetterU {

    getLetterPath(){
        let UPath = [ {x: 56.41, y: 54.82}, 
                      {x: 56.41, y: 50.87}, 
                      {x: 54.79, y: 50.87}, 
                      {x: 54.79, y: 51.10}, 
                      {x: 55.48, y: 51.28}, 
                      {x: 55.48, y: 55.23}
        ];

        UPath = UPath.concat(GraphPoints.cubicBezierCurve(
            {x: 55.48, y: 55.23},
            {x: 55.53, y: 56.01},
            {x: 56.02, y: 57.37},
            {x: 58.78, y: 55.80},
            50
        ));

        UPath = UPath.concat(
            {x: 58.78, y: 55.80},
            {x: 58.84, y: 56.28},
            {x: 60.45, y: 56.28},
            {x: 60.45, y: 56.02},
            {x: 59.64, y: 55.93},
            {x: 59.64, y: 50.87},
            {x: 58.02, y: 50.87},
            {x: 58.02, y: 51.10},
            {x: 58.78, y: 51.28},
            {x: 58.78, y: 55.50}
        );

        UPath = UPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 58.78, y: 55.50},
            {x: 56.36, y: 56.42},
            {x: 56.41, y: 54.82},
            50
        ));

        return UPath;
    }

    getGeometry(){
        let UPath = this.getLetterPath();
        let holeUpath = null;
        return ContextRender.getExtrusion(UPath, holeUpath, 0.6);
    }
}