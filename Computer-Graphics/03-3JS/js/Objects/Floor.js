export class Floor extends THREE.Mesh {
    constructor(width = 200, height = 200) {
        const geometry = new THREE.PlaneGeometry(width, height);
        const textureLoader = new THREE.TextureLoader();
        const texture = textureLoader.load("../../media/floor.jpg");
        const material = new THREE.MeshPhongMaterial({ map: texture, side: THREE.DoubleSide });
        super(geometry, material);
        this.rotation.x = -Math.PI / 2;
        this.position.y = -60;
    }
}