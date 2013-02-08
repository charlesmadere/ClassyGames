package edu.selu.android.classygames;


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

import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.Utilities;


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
	 * One of this class's callback methods. This is fired in the event that
	 * an error was detected in some of the data needed to instantiate this
	 * fragment.
	 */
	private ConfirmGameFragmentOnDataErrorListener confirmGameFragmentOnDataErrorListener;

	public interface ConfirmGameFragmentOnDataErrorListener
	{
		public void confirmGameFragmentOnDataError();
	}


	/**
	 * One of this class's callback methods. This is fired in the event that
	 * the current user confirms that they want to play against their selected
	 * Facebook friend.
	 */
	private ConfirmGameFragmentOnGameConfirmListener confirmGameFragmentOnGameConfirmListener;

	public interface ConfirmGameFragmentOnGameConfirmListener
	{
		public void confirmGameFragmentOnGameConfirm(final Person friend);
	}


	/**
	 * One of this class's callback methods. This is fired in the event that
	 * the current user decides that they do not want to play against their
	 * selected Facebook friend.
	 */
	private ConfirmGameFragmentOnGameDenyListener confirmGameFragmentOnGameDenyListener;

	public interface ConfirmGameFragmentOnGameDenyListener
	{
		public void confirmGameFragmentOnGameDeny();
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
				view = inflater.inflate(R.layout.confirm_game_fragment_loaded, null);
			}
			else
			{
				confirmGameFragmentOnDataErrorListener.confirmGameFragmentOnDataError();
			}
		}

		return view;
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (friend != null && friend.isValid())
		{
			final View view = getView();

			final TextView friendName = (TextView) view.findViewById(R.id.confirm_game_fragment_loaded_friend_name);
			friendName.setText(friend.getName());
			friendName.setTypeface(Utilities.TypefaceUtilities.getTypeface(getSherlockActivity().getAssets(), Utilities.TypefaceUtilities.SNELL_ROUNDHAND_BDSCR));

			final TextView description = (TextView) view.findViewById(R.id.confirm_game_fragment_loaded_description);
			description.setText(getString(R.string.confirm_game_fragment_loaded_description_text, friend.getName()));

			final Button buttonConfirm = (Button) view.findViewById(R.id.confirm_game_fragment_loaded_button_confirm);
			buttonConfirm.setTypeface(Utilities.TypefaceUtilities.getTypeface(getSherlockActivity().getAssets(), Utilities.TypefaceUtilities.BLUE_HIGHWAY_D));
			buttonConfirm.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					confirmGameFragmentOnGameConfirmListener.confirmGameFragmentOnGameConfirm(friend);
				}
			});

			final Button buttonDeny = (Button) view.findViewById(R.id.confirm_game_fragment_loaded_button_deny);
			buttonDeny.setTypeface(Utilities.TypefaceUtilities.getTypeface(getSherlockActivity().getAssets(), Utilities.TypefaceUtilities.BLUE_HIGHWAY_D));
			buttonDeny.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					confirmGameFragmentOnGameDenyListener.confirmGameFragmentOnGameDeny();
				}
			});

			final ImageView profilePicture = (ImageView) view.findViewById(R.id.confirm_game_fragment_loaded_friend_profile_picture);
			Utilities.getImageLoader(getSherlockActivity()).displayImage(Utilities.FacebookUtilities.GRAPH_API_URL + friend.getId() + Utilities.FacebookUtilities.GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL, profilePicture);
		}
	}


	@Override
	public void onAttach(Activity activity)
	// This makes sure that the Activity containing this Fragment has
	// implemented the callback interface. If the callback interface has not
	// been implemented, an exception is thrown.
	{
		super.onAttach(activity);

		try
		{
			confirmGameFragmentOnDataErrorListener = (ConfirmGameFragmentOnDataErrorListener) activity;
			confirmGameFragmentOnGameConfirmListener = (ConfirmGameFragmentOnGameConfirmListener) activity;
			confirmGameFragmentOnGameDenyListener = (ConfirmGameFragmentOnGameDenyListener) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}




	public boolean isLoaded()
	{
		return friend != null && friend.isValid();
	}


}
