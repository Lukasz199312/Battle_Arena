package com.mygdx.game;
//package com.mygdx.game;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Camera;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Transform;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import com.mygdx.client._Client;
//import com.mygdx.objects.GameObject;
//import com.mygdx.objects.IGameObject;
//import com.mygdx.objects.Player_net;
//import com.mygdx.objects.UniqueID;
//import com.mygdx.objects.player.__Player;
//
//public class CopyOfMyGdxGame extends ApplicationAdapter {
//	SpriteBatch batch;
//	Texture img;
//	Stage stage;
//	ArrayList<IGameObject> ObjectList = new ArrayList<IGameObject>();
//	Queue<IGameObject> queue;
//	BlockingQueue<Vector2> Player_Position_queue = new ArrayBlockingQueue<Vector2>(1);
//	OrthographicCamera camera = new OrthographicCamera();
//	
//	@Override
//	public void create () {
//		_Client client = new _Client(Thread.currentThread(), Player_Position_queue);
//		client.setObjectList(ObjectList);
//		queue = new LinkedList<IGameObject>();
//		client.setQueue(queue);
//		new Thread(client).start();
//		batch = new SpriteBatch();
//		stage = new Stage();
//		Gdx.input.setInputProcessor(stage);
//		
//
//		
//		Player_net player_net = new Player_net(null);
//		player_net.transform = new Transform(new Vector2(50, 50), 0);
//		player_net.texture = new Texture(Gdx.files.internal("Player_Texture.png"));
//		
//		
//		
//		
//	}
//
//	@Override
//	public void render () {
//		stage.getCamera().update();
//		float deltaTime = Gdx.graphics.getDeltaTime();
//		deltaTime = Math.min(deltaTime, 0.1f);
//		Gdx.gl.glClearColor(255, 255, 255, 0);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//		if(queue.isEmpty() == false){
//			IGameObject object = queue.remove();
//			__Player gameObject = new __Player(Player_Position_queue, new Transform(new Vector2(0, 0), 0));
//			gameObject.texture = new Texture(Gdx.files.internal("Player_Texture.png"));
//			stage.addActor(gameObject);
//		}
//		
//		stage.act();
//		stage.draw();
//		
//
//	}
//	
//	
//	@Override
//	public void resize(int width, int height) {
//		stage.getViewport().update(width, height);
//	}
//	
//	@Override
//	public void dispose() {
//		stage.dispose();
//	}
//}
