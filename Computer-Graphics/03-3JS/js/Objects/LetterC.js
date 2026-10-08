import { GraphPoints } from '../Utils/GraphPoints.js';
import { ContextRender } from '../Render/ContextRender.js';
export class LetterC {

    getLetterPath(){
        let CPath = [{x: 36.31, y: 51.6}, {x: 36.52, y: 52.59}, {x: 36.87, y: 52.59}, {x: 36.87, y: 51.1}];

        CPath = CPath.concat(GraphPoints.quadraticBezierCurve(
            {x: 36.87, y: 51.1},
            {x: 36.33, y: 50.75},
            {x: 35.17, y: 50.75},
            50
        ));

        CPath = CPath.concat(GraphPoints.cubicBezierCurve(
            {x: 35.17, y: 50.75},
            {x: 31.41, y: 50.75},
            {x: 31.83, y: 56.6},
            {x: 35.08, y: 56.6},
            50
        ));

        CPath = CPath.concat(GraphPoints.cubicBezierCurve(
            {x: 35.08, y: 56.6},
            {x: 35.32, y: 56.6},
            {x: 36.87, y: 56.47},
            {x: 36.87, y: 56.23},
            50
        ));

        CPath = CPath.concat({x: 36.87, y: 55.85});

        CPath = CPath.concat(GraphPoints.cubicBezierCurve(
            {x: 36.87, y: 55.85},
            {x: 35.56, y: 56.08},
            {x: 34.49, y: 56.23},
            {x: 34, y: 55.36},
            50
        ));

        CPath = CPath.concat(GraphPoints.cubicBezierCurve(
            {x: 34, y: 55.36},
            {x: 33.33, y: 54.16},
            {x: 33.52, y: 52.51},
            {x: 34, y: 51.75},
            50
        ));

        CPath = CPath.concat(GraphPoints.cubicBezierCurve(
            {x: 34, y: 51.75},
            {x: 34.55, y: 50.88},
            {x: 36.23, y: 51.17},
            {x: 36.31, y: 51.6},
            50
        ));
        return CPath;
    }

    getGeometry(){
        let CPath = this.getLetterPath();
        let holeCpath = null;
        return ContextRender.getExtrusion(CPath, holeCpath, 0.6);
    }
}