export class Wall extends THREE.Mesh {
    constructor(width = 200, height = 180) {
        const geometry = new THREE.PlaneGeometry(width, height);
        const textureLoader = new THREE.TextureLoader();
        const texture = textureLoader.load("../../media/blankBoard.jpg");
        const material = new THREE.MeshPhongMaterial({ map: texture, side: THREE.DoubleSide });
        super(geometry, material);
        this.position.z = -100;
        this.position.y = 30;
    }
}