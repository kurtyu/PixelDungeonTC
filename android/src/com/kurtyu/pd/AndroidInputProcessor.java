package com.kurtyu.pd;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import com.watabou.input.PDInputProcessor;

public class AndroidInputProcessor extends PDInputProcessor
{
	private Activity activity;

	public AndroidInputProcessor(Activity activity)
	{
		super();
		this.activity = activity;
	}

	@Override
	public void hideActivity()
	{
		activity.moveTaskToBack(true);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Touch touch = new Touch(screenX, screenY);
		pointers.put(pointer, touch);
		eventTouch.dispatch(touch);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		eventTouch.dispatch(pointers.remove(pointer).up());
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		pointers.get(pointer).update(screenX, screenY);
		eventTouch.dispatch(null);
		return true;
	}

	@Override
	public void setOrientation(boolean landscape)
	{
		activity.setRequestedOrientation( landscape ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
	}
}
