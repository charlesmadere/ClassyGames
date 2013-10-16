package com.charlesmadere.android.classygames.server;


import android.content.Context;
import com.charlesmadere.android.classygames.models.Person;


public final class ServerApiRegister extends ServerApi
{


	/**
	 * Creates a ServerApi object. This should be used to hit the NewRegId end
	 * point. If this constructor is used, then the user will see a
	 * ProgressDialog popup while this ServerApi object is running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 */
	protected ServerApiRegister(final Context context, final Listeners listeners)
	{
		super(context, listeners);
	}


	/**
	 * Creates a ServerApi object. This should be used to hit the NewRegId end
	 * point. This constructor also allows you to specify whether or not the
	 * user should see a ProgressDialog popup while this ServerApi object is
	 * running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 *
	 * @param showProgressDialog
	 * Set this to true if you want the user to see a ProgressDialog while this
	 * ServerApi object is running.
	 */
	protected ServerApiRegister(final Context context, final Listeners listeners, final boolean showProgressDialog)
	{
		super(context, listeners, showProgressDialog);
	}


	@Override
	protected String postToServer(final Person whoAmI)
	{
		return null;
	}


}
