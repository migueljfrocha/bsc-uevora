import { LetterA } from './LetterA.js';
import { ContextRender } from '../Render/ContextRender.js';

export class LetterA_accent extends LetterA {
    constructor() {
        super();
    }
    getLetterAccentPath(){
        let accentPath = [ {x: 45.67, y: 50.32}, 
                           {x: 45.35, y: 50.32}, 
                           {x: 45.35, y: 50.12}, 
                           {x: 46.50, y: 48.53},
                           {x: 47.75, y: 48.53},
                           {x: 47.75, y: 48.75}
                         ];
        return accentPath;
    }

    getGeometry(){
        let APath = super.getLetterPath();
        let holePath = super.getLetterHolePath();
        let A_Extrusion = ContextRender.getExtrusion(APath, holePath, 0.6);
        let accentPath = this.getLetterAccentPath();
        let Accent_Extrusion = ContextRender.getExtrusion(accentPath, null, 0.6);
        Accent_Extrusion.translate(0, 0, -23.5);
        A_Extrusion.translate(0, 0, -23.5);
        
        let combined = new THREE.Geometry();
        combined.merge(A_Extrusion);
        combined.merge(Accent_Extrusion);

        return combined;
    }
}