package com.example.linkminishdefender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCPlace;
import org.cocos2d.actions.instant.CCToggleVisibility;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRepeat;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.view.MotionEvent;

public class CapaJuego extends CCColorLayer {

	public interface mover {
		boolean linkMueve = false;
	}

	CGPoint location;
	private CCSprite fondo;
	private CCLabel texto;
	private CCLabel textoPuntos;
	private CGSize winSize;
	private CCAction accionBasica;
	private CCSprite link;
	private CCSprite minishIcono;
	private CCAnimation linkDerecha;
	private CCAnimation linkIzquierda;
	private CCAnimation morir;
	private ArrayList<CCSpriteFrame> framesMorir;
	private ArrayList<CCSpriteFrame> framesDerecha;
	private ArrayList<CCSpriteFrame> framesIzquierda;
	private LinkedList<CCSprite> disparos = new LinkedList<CCSprite>();
	private LinkedList<CCSprite> enemigos = new LinkedList<CCSprite>();
	public static int nivel = 1;
	public static int minish = 3;
	public static int puntos = 0;
	boolean movidoDerecha = false;
	boolean movidoIzquierda = false;
	boolean muriendo = false;
	private float enemigoY = 100f - nivel;

	protected CapaJuego(ccColor4B color) {
		super(color);

		this.setIsTouchEnabled(true);
		winSize = CCDirector.sharedDirector().displaySize();
		Context context = CCDirector.sharedDirector().getActivity();
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.sonidomorir);
		iniciarMusica();
		fondo = CCSprite.sprite("fondo.png");
		fondo.setPosition(CGPoint.ccp(winSize.width / 2.0f,
				winSize.height / 2.0f));
		addChild(fondo);
		crearAnimacionDerecha();
		crearAnimacionIzquierda();
		crearAnimacionMorir();
		link = CCSprite.sprite("link.png");
		link.setPosition(CGPoint.ccp(winSize.width / 2.0f, 25f));
		addChild(link);
		refrescarTexto();
		minishIcono = CCSprite.sprite("minish.png");
		minishIcono.setPosition(CGPoint.ccp(texto.getPosition().x+ 20,texto.getPosition().y));
		addChild(minishIcono);
		this.schedule("gameLogic", 1 / 40f);
		crearEnemigos();

	}

	private void refrescarTexto() {
		if (texto == null) {
			texto = CCLabel.makeLabel("" + minish + "", "DroidSans", 18);
			texto.setColor(ccColor3B.ccWHITE);
			texto.setPosition(15f, winSize.height - 15f);
			addChild(texto);
		} else {
			removeChild(texto, true);
			texto = CCLabel.makeLabel("" + minish + "", "DroidSans", 18);
			texto.setColor(ccColor3B.ccWHITE);
			texto.setPosition(15f, winSize.height - 15f);
			addChild(texto);
		}
		if(textoPuntos == null){
			textoPuntos = CCLabel.makeLabel("" + puntos + "", "DroidSans", 18);
			textoPuntos.setColor(ccColor3B.ccWHITE);
			textoPuntos.setPosition(winSize.width - 15f, winSize.height - 15f);
			addChild(textoPuntos);
		}
		else{
			removeChild(textoPuntos, true);
			textoPuntos = CCLabel.makeLabel("" + puntos + "", "DroidSans", 18);
			textoPuntos.setColor(ccColor3B.ccWHITE);
			textoPuntos.setPosition(winSize.width - 25f, winSize.height - 15f);
			addChild(textoPuntos);
		}
	}

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new CapaJuego(ccColor4B.ccc4(255, 255, 255, 255));

		scene.addChild(layer);

		return scene;
	}

	public void gameLogic(float dt){
		refrescarTexto();
		LinkedList<CCSprite> listaEnemigosParaDestruir = new LinkedList<CCSprite>();
		LinkedList<CCSprite> listaDisparosParaDestruir = new LinkedList<CCSprite>();
		for (CCSprite enemigo : enemigos) {
			CGRect areaEnemigo = CGRect.make(
					enemigo.getPosition().x - enemigo.getContentSize().width
							/ 2.0f,
					enemigo.getPosition().y - enemigo.getContentSize().height
							/ 2.0f, enemigo.getContentSize().width,
					enemigo.getContentSize().height);
			LinkedList<CGRect> areaDisparos = new LinkedList<CGRect>();
			for (CCSprite bala : disparos) {
				areaDisparos.add(CGRect.make(
						bala.getPosition().x - bala.getContentSize().width
								/ 2.0f,
						bala.getPosition().y - bala.getContentSize().height
								/ 2.0f, bala.getContentSize().width,
						bala.getContentSize().height));
			}
			for (CCSprite disparo : disparos) {
				for (CGRect areaDisparo : areaDisparos) {
					if (CGRect.intersects(areaEnemigo, areaDisparo)) {
						if (areaDisparo.contains(disparo.getPosition().x,
								disparo.getPosition().y)) {
							listaEnemigosParaDestruir.add(enemigo);
							listaDisparosParaDestruir.add(disparo);
							CGPoint enemigoPos = CGPoint.ccp(enemigo.getPosition().x, enemigo.getPosition().y);
							removeChild(enemigo,true);
							removeChild(disparo, true);
							enemigo = morir();
							enemigo.setPosition(enemigoPos);
							addChild(enemigo);
							Context context = CCDirector.sharedDirector().getActivity();
							SoundEngine.sharedEngine().playEffect(context, R.raw.sonidomorir);
							puntos =puntos +100;
						}
					}
				}
			}

		}
		for (CCSprite enemigo : enemigos) {
			if (enemigo.getPosition().y < -20) {
				minish--;
				puntos = puntos - 100;
				if (minish > 0) {
					listaEnemigosParaDestruir.add(enemigo);
				} else {
					CCDirector.sharedDirector()
							.replaceScene(CapaPerder.scene());
				}
			}
		}

		if (listaEnemigosParaDestruir.size() > 0) {
			for (CCSprite enemigo : listaEnemigosParaDestruir) {
				enemigos.remove(enemigo);
			}
		}
		if (listaDisparosParaDestruir.size() > 0) {
			for (CCSprite disparo : listaDisparosParaDestruir) {
				disparos.remove(disparo);
			}
		}

		if (enemigos.isEmpty()) {
			CCDirector.sharedDirector().replaceScene(CapaGanar.scene());
		}
	}

	private void crearEnemigos() {
		int inserciones = 0;
		while (inserciones < 7 * nivel) {
			CCSprite enemigo = animar();
			float posX = 40 + new Random().nextInt((int) winSize.width - 40);
			float posY = winSize.height
					+ new Random().nextInt((int) winSize.height * 2);
			enemigo.setPosition(CGPoint.ccp(posX, posY));
			CCMoveTo mover = CCMoveTo.action(enemigoY,
					CGPoint.ccp(enemigo.getPosition().x, -5000));
			CCCallFunc fin = CCCallFuncN.action(this, "finEnemigo");
			CCSequence accion = CCSequence.actions(mover, fin);
			enemigo.runAction(accion);
			/* La posicionX del bloque esta fuera de la pantalla */
			enemigos.add(enemigo);
			addChild(enemigo);
			inserciones++;
		}
	}

	private CCSprite animar() {
		CCSprite enemigo = null;
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				"animacionenemigo.plist");
		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
		for (int i = 1; i <= 2; ++i) {
			CCSpriteFrameCache.sharedSpriteFrameCache();
			frames.add(CCSpriteFrameCache.spriteFrameByName("enemigo" + i
					+ ".png"));
		}
		CCAnimation base = CCAnimation.animation("basica", 0.1f, frames);
		enemigo = CCSprite.sprite(frames.get(0));
		CCAction accionBasica = CCRepeatForever.action(CCAnimate.action(base,
				true));
		enemigo.runAction(accionBasica);
		return enemigo;
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {

		float distancia = Math.abs(link.getPosition().x - event.getX());
		if (event.getX() > link.getPosition().x+5) {
			if (!movidoDerecha) {
				movidoIzquierda = false;
				removeChild(link, true);
				CGPoint linkPos = CGPoint.ccp(link.getPosition().x, 25f);
				CCSprite nLink = animarDerecha();
				link = nLink;
				link.setPosition(linkPos);
				addChild(link);
				movidoDerecha = true;
			}
		} else if (event.getX() < link.getPosition().x-5) {
			if (!movidoIzquierda) {
				movidoDerecha = false;
				removeChild(link, true);
				CGPoint linkPos = CGPoint.ccp(link.getPosition().x, 25f);
				CCSprite nLink = animarIzquierda();
				link = nLink;
				link.setPosition(linkPos);
				addChild(link);
				movidoIzquierda = true;
			}
		} else {
			removeChild(link, true);
			CGPoint linkPos = CGPoint.ccp(link.getPosition().x, 25f);
			link = CCSprite.sprite("link.png");
			link.setPosition(linkPos);
			addChild(link);
			movidoIzquierda = false;
			movidoDerecha = false;
		}

		CCMoveTo moverLinkHaciaPunto = CCMoveTo.action(distancia / 400,
				CGPoint.ccp(event.getX(), link.getPosition().y));
		CCCallFuncN movimientoFinzalizado = CCCallFuncN.action(this,
				"finalMovimientoLink");
		CCSequence actions = CCSequence.actions(moverLinkHaciaPunto,
				movimientoFinzalizado);
		link.runAction(actions);

		return super.ccTouchesMoved(event);
	}

	private void crearAnimacionIzquierda() {
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				"linkizquierda.plist");
		framesIzquierda = new ArrayList<CCSpriteFrame>();
		for (int i = 5; i <= 10; ++i) {
			CCSpriteFrameCache.sharedSpriteFrameCache();
			framesIzquierda.add(CCSpriteFrameCache.spriteFrameByName("link" + i
					+ ".png"));
		}
		linkIzquierda = CCAnimation.animation("basica", 0.1f, framesIzquierda);
	}

	public CCSprite animarIzquierda() {
		CCSprite link = null;
		link = CCSprite.sprite(framesIzquierda.get(0));
		CCAction accionBasica = CCRepeatForever.action(CCAnimate.action(
				linkIzquierda, true));
		link.runAction(accionBasica);
		return link;
	}

	public CCSprite animarDerecha() {
		CCSprite link = null;
		link = CCSprite.sprite(framesDerecha.get(0));
		CCAction accionBasica = CCRepeatForever.action(CCAnimate.action(
				linkDerecha, true));
		link.runAction(accionBasica);
		return link;
	}

	private void crearAnimacionDerecha() {
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				"linkderecha.plist");
		framesDerecha = new ArrayList<CCSpriteFrame>();
		for (int i = 5; i <= 10; ++i) {
			CCSpriteFrameCache.sharedSpriteFrameCache();
			framesDerecha.add(CCSpriteFrameCache.spriteFrameByName("link" + i
					+ ".png"));
		}
		linkDerecha = CCAnimation.animation("basica", 0.1f, framesDerecha);

	}
	
	private void crearAnimacionMorir() {
		CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames(
				"morir.plist");
		framesMorir = new ArrayList<CCSpriteFrame>();
		for (int i = 1; i <= 7; ++i) {
			CCSpriteFrameCache.sharedSpriteFrameCache();
			framesMorir.add(CCSpriteFrameCache.spriteFrameByName("morir" + i
					+ ".png"));
		}
		morir = CCAnimation.animation("basica", 0.1f, framesMorir);

	}
	
	public CCSprite morir() {
		CCSprite enemigo = null;
		enemigo = CCSprite.sprite(framesMorir.get(0));
		accionBasica = CCRepeat.action(CCAnimate.action(
				morir, true), 1);
		enemigo.runAction(accionBasica);
		return enemigo;
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		crearDisparos();
		return true;
	}

	

	

	private void crearDisparos() {
		CCSprite disparo = CCSprite.sprite("disparo.png");
		disparo.setPosition(CGPoint.ccp(link.getPosition().x,
				link.getPosition().y + 30));
		CCMoveTo mover = CCMoveTo.action(1f,
				CGPoint.ccp(disparo.getPosition().x, winSize.height + 10));
		CCCallFunc fin = CCCallFuncN.action(this, "finDisparo");
		CCSequence accion = CCSequence.actions(mover, fin);
		disparos.add(disparo);
		addChild(disparo);
		disparo.runAction(accion);
	}

	private void finDisparo(Object sender) {
		for (CCSprite disparo : disparos) {
			removeChild(disparo, true);
			disparos.remove(disparo);
		}
	}

	private void finEnemigo(Object sender) {
		for (CCSprite enemigo : enemigos) {
			if (enemigo.getPosition().y == 0)
				removeChild(enemigo, true);
			enemigos.remove(enemigo);
		}
	}
	public static void iniciarMusica() {
		/** Sonido */
		Context context = CCDirector.sharedDirector().getActivity();
		SoundEngine.sharedEngine().stopSound();
		SoundEngine.purgeSharedEngine();
		SoundEngine.sharedEngine().playSound(context, R.raw.sonidofondo, true);
	}

	public static void pausarMusica() {
		// Sonido
		SoundEngine.sharedEngine().stopSound();
	}
	
}
