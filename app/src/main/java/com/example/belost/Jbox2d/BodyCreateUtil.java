package com.example.belost.Jbox2d;

import android.graphics.Bitmap;


import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class BodyCreateUtil {
    World world;
    public BodyCreateUtil(World world){
        this.world=world;
    }
    public Circle createCircle(Bitmap bitmap,float x, float y, float ridius, int color,boolean isStatic,int id){
        BodyDef bd=new BodyDef();
        if (isStatic){ bd.type= BodyType.STATIC; }else {bd.type=BodyType.DYNAMIC;}
        bd.position.set(x/Params.RATE,y/Params.RATE);
        Body body=world.createBody(bd);
        CircleShape cs=new CircleShape();
        cs.setRadius(ridius/Params.RATE);
        FixtureDef fd=new FixtureDef();
        fd.density=1;
        fd.friction=1;
        fd.restitution=0.5f;
        fd.shape=cs;
        body.createFixture(fd);
        body.m_userData=id;
        return new Circle(body,bitmap,ridius,color);
    }
    public Rect createRect(Bitmap bitmap,float x, float y, float halfWidth,float halfHeight, int color,boolean isStatic,int type) {
        BodyDef bd = new BodyDef();
        if (isStatic) {
            bd.type = BodyType.STATIC;
        } else {
            bd.type = BodyType.DYNAMIC;
        }
        bd.position.set(x / Params.RATE, y / Params.RATE);
        PolygonShape pd = new PolygonShape();
        pd.setAsBox(halfWidth / Params.RATE, halfHeight / Params.RATE);
        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.7f;
        fd.shape = pd;
        Body body = world.createBody(bd);
        body.m_userData=type;
        body.createFixture(fd);
        return new Rect(body, bitmap, x, y, halfWidth, halfHeight, color);
    }


}

