import { GraphPoints } from '../Utils/GraphPoints.js';
import { ContextRender } from '../Render/ContextRender.js';
export class LetterA {

    getLetterPath(){
        let APath = [ {x: 47.66, y: 52.02}, 
                      {x: 47.66, y: 55.93}, 
                      {x: 48.46, y: 56.02}, 
                      {x: 48.46, y: 56.28}, 
                      {x: 46.86, y: 56.28}, 
                      {x: 46.8, y: 55.8}
        ];

         APath = APath.concat(GraphPoints.cubicBezierCurve(
            {x: 46.8, y: 55.8},
            {x: 44.88, y: 57.37},
            {x: 43.76, y: 55.92},
            {x: 43.83, y: 54.79},
            50
        ));

        APath = APath.concat(GraphPoints.cubicBezierCurve(
            {x: 43.83, y: 54.79},
            {x: 43.95, y: 53.08},
            {x: 44.79, y: 53.32},
            {x: 46.8, y: 53.19},
            50
        ));

        APath = APath.concat({x: 46.8, y: 52.18});

        APath = APath.concat(GraphPoints.cubicBezierCurve(
            {x: 46.8, y: 52.18},
            {x: 46.84, y: 51.1},
            {x: 46.1, y: 50.98},
            {x: 44.79, y: 51.42},
            50
        ));

        APath = APath.concat({x: 44.67, y: 52.18}, { x: 44.37, y: 52.18 }, { x: 44.37, y: 51.1 });

        APath = APath.concat(GraphPoints.cubicBezierCurve(
            { x: 44.37, y: 51.1 },
            {x: 45.74, y: 50.62},
            {x: 47.66, y: 50.55},
            {x: 47.66, y: 52.02},
            50
        ));

        return APath;
    }
    getLetterHolePath(){
        let holeApath = [ {x: 46.8, y: 53.56}, 
                          {x: 46.8, y: 55.50}
                        ];
        holeApath = holeApath.concat(GraphPoints.cubicBezierCurve(
            {x: 46.8, y: 55.5},
            {x: 44.26, y: 56.82},
            {x: 44.98, y: 53.59},
            {x: 44.79, y: 54.28},
            50
        ));

        holeApath = holeApath.concat(GraphPoints.cubicBezierCurve(
            {x: 44.79, y: 54.28},
            {x: 44.98, y: 53.59},
            {x: 46.55, y: 53.61},
            {x: 46.8, y: 53.56},
            50
        ));

        return holeApath;

    }
    getGeometry(){
        let APath = this.getLetterPath();
        let holeApath = this.getLetterHolePath();
        return ContextRender.getExtrusion(APath, holeApath, 0.6);
    }
}