package com.charlesmadere.android.classygames;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ConfirmGameFragment extends SherlockFragment
{


	public final static String KEY_FRIEND_ID = "KEY_FRIEND_ID";
	public final static String KEY_FRIEND_NAME = "KEY_FRIEND_NAME";




	/**
	 * The Person object that will be shown on this Fragment's layout. The user
	 * must confirm whether or not they want to play against this friend.
	 */
	private Person friend;




	/**
	 * Object that allows us to run any of the methods that are defined in the
	 * ConfirmGameFragmentListeners interface.
	 */
	private ConfirmGameFragmentListeners listeners;


	/**
	 * A bunch of listener methods for this Fragment.
	 */
	public interface ConfirmGameFragmentListeners
	{


		/**
		 * This is fired during this Fragment's onCreateOptionsMenu() method.
		 * Checks to see if the current device is considered by Android to be
		 * small. This be basically every phone.
		 * 
		 * @return
		 * Returns true if the current device is small.
		 */
		public boolean isDeviceSmall();


		/**
		 * This is fired in the event that an error was detected with some of
		 * the data needed to instantiate a game.
		 */
		public void onDataError();


		/**
		 * This is fired in the event that the current device's user clicks the
		 * "Start Game!" button. This means that they've decided to play a game
		 * against the chosen friend.
		 * 
		 * @param friend
		 * The Facebook friend that the user has decided to play against.
		 */
		public void onGameConfirm(final Person friend);


		/**
		 * This is fired in the event that the current device's user clicks the
		 * "Nevermind..." button. This means that they've decided that they'd
		 * rather not play against the chosen friend.
		 */
		public void onGameDeny();


	}




	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.confirm_game_fragment, null);

		final Bundle arguments = getArguments();

		if (arguments != null && !arguments.isEmpty())
		{
			final long friendId = arguments.getLong(KEY_FRIEND_ID);
			final String friendName = arguments.getString(KEY_FRIEND_NAME);

			if (Person.isIdValid(friendId) && Person.isNameValid(friendName))
			{
				friend = new Person(friendId, friendName);
			}
			else
			{
				listeners.onDataError();
			}
		}

		return view;
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		final View view = getView();

		final TextView friendName = (TextView) view.findViewById(R.id.confirm_game_fragment_friend_name);
		friendName.setText(friend.getName());
		friendName.setTypeface(TypefaceUtilities.getTypeface(getSherlockActivity().getAssets(), TypefaceUtilities.SNELL_ROUNDHAND_BDSCR));

		final TextView description = (TextView) view.findViewById(R.id.confirm_game_fragment_description);
		description.setText(getString(R.string.are_you_sure_that_you_want_to_start_a_game_with_x, friend.getName()));

		final Button buttonConfirm = (Button) view.findViewById(R.id.confirm_game_fragment_button_confirm);
		buttonConfirm.setTypeface(TypefaceUtilities.getTypeface(getSherlockActivity().getAssets(), TypefaceUtilities.BLUE_HIGHWAY_D));
		buttonConfirm.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				listeners.onGameConfirm(friend);
			}
		});

		final Button buttonDeny = (Button) view.findViewById(R.id.confirm_game_fragment_button_deny);
		buttonDeny.setTypeface(TypefaceUtilities.getTypeface(getSherlockActivity().getAssets(), TypefaceUtilities.BLUE_HIGHWAY_D));
		buttonDeny.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				listeners.onGameDeny();
			}
		});

		final ImageView profilePicture = (ImageView) view.findViewById(R.id.confirm_game_fragment_friend_profile_picture);
		Utilities.getImageLoader(getSherlockActivity()).displayImage(FacebookUtilities.GRAPH_API_URL + friend.getId() + FacebookUtilities.GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL, profilePicture);
	}


	@Override
	public void onAttach(final Activity activity)
	// This makes sure that the Activity containing this Fragment has
	// implemented the callback interface. If the callback interface has not
	// been implemented, an exception is thrown.
	{
		super.onAttach(activity);

		try
		{
			listeners = (ConfirmGameFragmentListeners) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		if (listeners.isDeviceSmall())
		{
			menu.removeItem(R.id.generic_refresh_menu_refresh);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}


}
