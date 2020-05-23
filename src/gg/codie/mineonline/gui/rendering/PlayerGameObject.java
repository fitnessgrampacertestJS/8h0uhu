package gg.codie.mineonline.gui.rendering;

import gg.codie.mineonline.gui.rendering.models.RawModel;
import gg.codie.mineonline.gui.rendering.models.TexturedModel;
import gg.codie.mineonline.gui.rendering.shaders.StaticShader;
import gg.codie.mineonline.gui.rendering.textures.ModelTexture;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class PlayerGameObject extends GameObject {

    private final Loader loader;
    private final StaticShader shader;

    private final int SKIN_WIDTH = 64;
    private final int SKIN_HEIGHT = 64;

    public PlayerGameObject(String name, Loader loader, StaticShader shader, Vector3f localPosition, float rotX, float rotY, float rotZ, float scale)
    {
        super(name);

        this.loader = loader;
        this.shader = shader;

        this.localPosition = localPosition;
        this.localXRot = rotX;
        this.localYRot = rotY;
        this.localZRot = rotZ;
        this.scale = scale;

        playerHead = addBox(new Vector3f(-4f, -8f, -4f), 8, 8, 8, new Vector3f(0, -4, 0), new Vector3f(0, 36, 0), TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, SKIN_HEIGHT),
                new Vector2f(24, 8), new Vector2f(8, 8),
                new Vector2f(8, 8), new Vector2f(8, 8),
                new Vector2f(16, 8), new Vector2f(8, 8),
                new Vector2f(0, 8), new Vector2f(8, 8),
                new Vector2f(8, 0), new Vector2f(8, 8),
                new Vector2f(16, 0), new Vector2f(8, 8)
                ));


        playerHeadwear = addHeadwear(new Vector3f(-4f, -8f, -4f), 8, 8, 8, TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, SKIN_HEIGHT),
                new Vector2f(56, 8), new Vector2f(8, 8),
                new Vector2f(40, 8), new Vector2f(8, 8),
                new Vector2f(48, 8), new Vector2f(8, 8),
                new Vector2f(32, 8), new Vector2f(8, 8),
                new Vector2f(40, 0), new Vector2f(8, 8),
                new Vector2f(48, 0), new Vector2f(8, 8)
        ));

        playerHeadwear.setScale(1.1f);

        playerBody = addBox(new Vector3f(-4, 0, 2), 8, 12, 4, new Vector3f(0, -4, -4), new Vector3f(0, 16, 0), TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, SKIN_HEIGHT),
                new Vector2f(32, 20), new Vector2f(8, 12),
                new Vector2f(20, 20), new Vector2f(8, 12),
                new Vector2f(28, 20), new Vector2f(4, 12),
                new Vector2f(16, 20), new Vector2f(4, 12),
                new Vector2f(20, 16), new Vector2f(8, 4),
                new Vector2f(28, 16), new Vector2f(8, 4)
        ));

        playerRightArm = addBox(new Vector3f(-3, -2, -2), 4, 12, 4, new Vector3f(1, -10, 0), new Vector3f(6, 24, 0), TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, SKIN_HEIGHT),
                new Vector2f(52, 20), new Vector2f(4, 12),
                new Vector2f(44, 20), new Vector2f(4, 12),
                new Vector2f(48, 20), new Vector2f(4, 12),
                new Vector2f(40, 20), new Vector2f(4, 12),
                new Vector2f(44, 16), new Vector2f(4, 4),
                new Vector2f(48, 16), new Vector2f(4, 4)
        ));


        playerLeftArm = addBox(new Vector3f(-1, -2, -2), 4, 12, 4, new Vector3f(-1, -10, 0), new Vector3f(-6, 24, 0), TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, SKIN_HEIGHT),
                new Vector2f(52, 20), new Vector2f(4, 12),
                new Vector2f(44, 20), new Vector2f(4, 12),
                new Vector2f(48, 20), new Vector2f(4, 12),
                new Vector2f(40, 20), new Vector2f(4, 12),
                new Vector2f(44, 16), new Vector2f(4, 4),
                new Vector2f(48, 16), new Vector2f(4, 4)
        ));


        playerRightLeg = addBox(new Vector3f(-2, 0, -2), 4 , 12, 4, new Vector3f(-2, -12, 0), new Vector3f(4, 12, 0), TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, SKIN_HEIGHT),
                new Vector2f(12, 20), new Vector2f(4, 12),
                new Vector2f(4, 20), new Vector2f(4, 12),
                new Vector2f(8, 20), new Vector2f(4, 12),
                new Vector2f(0, 20), new Vector2f(4, 12),
                new Vector2f(4, 16), new Vector2f(4, 4),
                new Vector2f(8, 16), new Vector2f(4, 4)
        ));

        playerLeftLeg = addBox(new Vector3f(-2, 0, -2), 4 , 12, 4, new Vector3f(2, -12, 0 ), new Vector3f(-4, 12, 0),  TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, SKIN_HEIGHT),
                new Vector2f(12, 20), new Vector2f(4, 12),
                new Vector2f(4, 20), new Vector2f(4, 12),
                new Vector2f(8, 20), new Vector2f(4, 12),
                new Vector2f(0, 20), new Vector2f(4, 12),
                new Vector2f(4, 16), new Vector2f(4, 4),
                new Vector2f(8, 16), new Vector2f(4, 4)
        ));

        playerCloak = addCape(new Vector3f(-5f, 0f, -1f), 10, 16, 1, new Vector3f(0, -16, 0), new Vector3f(0, 24, -2), TextureHelper.getCubeTextureCoords(new Vector2f(SKIN_WIDTH, 32),
                new Vector2f(1, 1), new Vector2f(10, 16),
                new Vector2f(11, 1), new Vector2f(10, 16),
                new Vector2f(1, 0), new Vector2f(1, 16),
                new Vector2f(21, 1), new Vector2f(1, 16),
                new Vector2f(1, 0), new Vector2f(10, 1),
                new Vector2f(11, 0), new Vector2f(10, 1)
        ));
    }

    public PlayerGameObject(String name, Loader loader, StaticShader shader) {
        this(name, loader, shader, new Vector3f(0, 0, 0), 0, 0, 0, 1);
    }

    private GameObject addBox(Vector3f begin, int width, int height, int depth, Vector3f position, Vector3f pivotPosition, float[] textureCoords) {
        RawModel model = loader.loadBoxToVAO(begin,
                new Vector3f(begin.x + width, begin.y + height, begin.z + depth),
                textureCoords);

        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("codie"));
        TexturedModel texturedModel =  new TexturedModel(model, modelTexture);

        GameObject box = new GameObject("box", texturedModel, position, 0, 0, 0, 1);

        GameObject pivot = new GameObject("pivot", pivotPosition, 0, 0, 0, 1);

        addChild(pivot);
        pivot.addChild(box);

        return pivot;
    }

    private GameObject addHeadwear(Vector3f begin, int width, int height, int depth, float[] textureCoords) {
        RawModel model = loader.loadBoxToVAO(begin,
                new Vector3f(begin.x + width, begin.y + height, begin.z + depth),
                textureCoords);

        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("codie"));
        TexturedModel texturedModel =  new TexturedModel(model, modelTexture);

        GameObject box = new GameObject("box", texturedModel, new Vector3f(0, -3.5f, 0), 0, 0, 0, 1);

        playerHead.addChild(box);

        return box;
    }

    private GameObject addCape(Vector3f begin, int width, int height, int depth, Vector3f position, Vector3f pivotPosition,  float[] textureCoords) {
        RawModel model = loader.loadBoxToVAO(begin,
                new Vector3f(begin.x + width, begin.y + height, begin.z + depth),
                textureCoords);

        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("cape"));
        TexturedModel texturedModel =  new TexturedModel(model, modelTexture);

        GameObject box = new GameObject("box",texturedModel, position, 0, 0, 0, 1);

        GameObject pivot = new GameObject("pivot", pivotPosition, 0, 0, 0, 1);

        addChild(pivot);
        pivot.addChild(box);

        return pivot;
    }

    public GameObject playerHead;
    public GameObject playerHeadwear;
    public GameObject playerBody;
    public GameObject playerRightArm;
    public GameObject playerLeftArm;
    public GameObject playerRightLeg;
    public GameObject playerLeftLeg;
    public GameObject playerCloak;
}
