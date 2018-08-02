package com.app.Listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;




/**自定义菜单监听器
 * @author ERIC
 *
 */
public class MenuMouseListener implements MouseListener{
	
	Class clazz = null;
	String executeFunctionName;
	Method m;
	Class[] argTypes;
	Object[] objects;
	
	public MenuMouseListener(Class clazz,String executeFunctionName,Object[] objects) throws NullPointerException,NoSuchMethodException{
		if(clazz==null || executeFunctionName==null || "".equals(executeFunctionName)){
			throw new NullPointerException();
		}
		if(objects!=null && objects.length!=0){
			this.objects = objects;
			argTypes = new Class[objects.length];
			for(int i = 0 ; i < objects.length ; i++){
				argTypes[i]=objects[i].getClass();
			}
		}
		this.clazz = clazz;
		this.executeFunctionName = executeFunctionName;
		if(objects!=null && objects.length!=0){
			m = clazz.getDeclaredMethod(executeFunctionName,argTypes);
		}else{
			m = clazz.getDeclaredMethod(executeFunctionName);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
			try {
				if(objects!=null){
					m.invoke(clazz.newInstance(),objects);
				}
				else{
					m.invoke(clazz.newInstance());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
