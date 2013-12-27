package com.charlesmadere.android.classygames;


import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class ConfirmGameFragment extends BaseFragment
{


	private final static String KEY_FRIEND = "KEY_FRIEND";




	/**
	 * The AlertDialog that is shown whenever the user decides to play a game
	 * against the chosen friend. This AlertDialog asks the user to choose
	 * which game they want to play.
	 */
	private AlertDialog alertDialog;


	/**
	 * The Person object that will be shown on this Fragment's layout. The user
	 * must confirm whether or not they want to play against this friend.
	 */
	private Person friend;




	/**
	 * Object that allows us to run any of the methods that are defined in the
	 * Listeners interface.
	 */
	private Listeners listeners;


	/**
	 * A bunch of listener methods for this Fragment.
	 */
	public interface Listeners
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
		 * This is fired in the event that the current device's user clicks the
		 * "Start Game!" button. This means that they've decided to play a game
		 * against the chosen friend.
		 * 
		 * @param friend
		 * The Facebook friend that the user has decided to play against.
		 *
		 * @param whichGame
		 * Which game that the user decided to play. This could be checkers,
		 * chess...
		 */
		public void onGameConfirm(final Person friend, final byte whichGame);


		/**
		 * This is fired in the event that the current device's user clicks the
		 * "Nevermind..." button. This means that they've decided that they'd
		 * rather not play against the chosen friend.
		 */
		public void onGameDeny();


	}




	public static ConfirmGameFragment newInstance(final Person friend)
	{
		final Bundle arguments = new Bundle();
		arguments.putSerializable(KEY_FRIEND, friend);

		final ConfirmGameFragment fragment = new ConfirmGameFragment();
		fragment.setArguments(arguments);

		return fragment;
	}




	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		final Bundle arguments = getArguments();
		friend = (Person) arguments.getSerializable(KEY_FRIEND);

		final View view = getView();
		final TextView friendsName = (TextView) view.findViewById(R.id.confirm_game_fragment_friend_name);
		friendsName.setText(friend.getName());

		final TextView description = (TextView) view.findViewById(R.id.confirm_game_fragment_description);
		description.setText(getString(R.string.are_you_sure_that_you_want_to_start_a_game_with_x, friend.getName()));

		final Button confirm = (Button) view.findViewById(R.id.confirm_game_fragment_button_confirm);
		confirm.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				// TODO
				// uncomment this and then delete the line below when chess is complete
				// getAlertDialog().show();

				listeners.onGameConfirm(friend, Game.WHICH_GAME_CHECKERS);
			}
		});

		final Button deny = (Button) view.findViewById(R.id.confirm_game_fragment_button_deny);
		deny.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				listeners.onGameDeny();
			}
		});

		final Activity activity = getActivity();
		final String friendsPictureURL = FacebookUtilities.getFriendsPictureLarge(activity, friend.getId());
		final ImageView profilePicture = (ImageView) view.findViewById(R.id.confirm_game_fragment_friend_profile_picture);
		Utilities.getImageLoader().displayImage(friendsPictureURL, profilePicture);
	}


	@Override
	public void onAttach(final Activity activity)
	{
		super.onAttach(activity);
		listeners = (Listeners) activity;
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		if (listeners.isDeviceSmall())
		{
			menu.removeItem(R.id.friends_list_fragment_menu_refresh);
		}

		menu.removeItem(R.id.friends_list_fragment_menu_search);

		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	protected int getConventView()
	{
		return R.layout.confirm_game_fragment;
	}




	/**
	 * Builds the AlertDialog that asks the user which game they'd like to play
	 * against their friend.
	 *
	 * @return
	 * Returns the AlertDialog completely built and ready to go. Simply use its
	 * show() method to draw it onto the screen.
	 */
	@SuppressWarnings("deprecation")
	private AlertDialog getAlertDialog()
	{
		if (alertDialog == null)
		{
			final Activity activity = getActivity();
			final LayoutInflater inflater = activity.getLayoutInflater();
			final View dialogView = inflater.inflate(R.layout.choose_which_game_dialog, null);

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			{
				final BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_subtle_gray);
				background.setAntiAlias(true);
				background.setDither(true);
				background.setFilterBitmap(true);
				background.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
				dialogView.setBackgroundDrawable(background);
			}

			final Button checkers = (Button) dialogView.findViewById(R.id.choose_which_game_dialog_button_checkers);
			checkers.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					{
						v.setActivated(true);
					}

					listeners.onGameConfirm(friend, Game.WHICH_GAME_CHECKERS);
				}
			});

			final Button chess = (Button) dialogView.findViewById(R.id.choose_which_game_dialog_button_chess);
			chess.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					{
						v.setActivated(true);
					}

					listeners.onGameConfirm(friend, Game.WHICH_GAME_CHESS);
				}
			});

			alertDialog = new AlertDialog.Builder(getActivity())
				.setView(dialogView)
				.create();
		}

		return alertDialog;
	}


}
