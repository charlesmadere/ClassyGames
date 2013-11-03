package com.charlesmadere.android.classygames.settings;


import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.text.SpannableString;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.Typefaces;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class GameSettingsFragment extends PreferenceFragment
{


	/**
	 * Object that allows us to run any of the methods that are defined in the
	 * GameSettingsFragmentListeners interface.
	 */
	private GameSettingsFragmentListeners listeners;


	/**
	 * A bunch of listener methods for this Fragment.
	 */
	public interface GameSettingsFragmentListeners
	{


		/**
		 * This method is called whenever the user changes the opponent's
		 * checkers piece color.
		 *
		 * @param playerCheckersPieceColor
		 * The ListPreference object for the player's checkers piece color.
		 *
		 * @param newValue
		 * The new color value that the player has selected. Note that this
		 * value has not been saved in the app's shared preferences system yet.
		 *
		 * @return
		 * Return true if you want to save this new color. False if you do not
		 * want to save this new color.
		 */
		public boolean onOpponentCheckersPieceColorPreferenceChange(final ListPreference playerCheckersPieceColor,
			final Object newValue);


		/**
		 * This method is called whenever the user changes the player's
		 * checkers piece color.
		 * 
		 * @param opponentCheckersPieceColor
		 * The ListPreference object for the opponent's checkers piece color.
		 * 
		 * @param newValue
		 * The new color value that the player has selected. Note that this
		 * value has not been saved in the app's shared preferences system yet.
		 * 
		 * @return
		 * Return true if you want to save this new color. False if you do not
		 * want to save this new color.
		 */
		public boolean onPlayerCheckersPieceColorPreferenceChange(final ListPreference opponentCheckersPieceColor,
			final Object newValue);


		/**
		 * This method is called whenever the user changes the opponent's
		 * chess piece color.
		 *
		 * @param playerChessPieceColor
		 * The ListPreference object for the player's chess piece color.
		 *
		 * @param newValue
		 * The new color value that the player has selected. Note that this
		 * value has not been saved in the app's shared preferences system yet.
		 *
		 * @return
		 * Return true if you want to save this new color. False if you do not
		 * want to save this new color.
		 */
		public boolean onOpponentChessPieceColorPreferenceChange(final ListPreference playerChessPieceColor,
			final Object newValue);


		/**
		 * This method is called whenever the user changes the player's chess
		 * piece color.
		 *
		 * @param opponentChessPieceColor
		 * The ListPreference object for the opponent's chess piece color.
		 *
		 * @param newValue
		 * The new color value that the player has selected. Note that this
		 * value has not been saved in the app's shared preferences system yet.
		 *
		 * @return
		 * Return true if you want to save this new color. False if you do not
		 * want to save this new color.
		 */
		public boolean onPlayerChessPieceColorPreferenceChange(final ListPreference opponentChessPieceColor,
			final Object newValue);


	}




	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final String actionBarTitle = getString(R.string.game_settings);
		final SpannableString styledActionBarTitle = Utilities.makeStyledString(actionBarTitle, Typefaces.getBlueHighway());
		getActivity().getActionBar().setTitle(styledActionBarTitle);

		addPreferencesFromResource(R.xml.settings_game);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getView().setBackgroundResource(R.drawable.bg_bright);

		final ListPreference playersCheckersPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_players_checkers_piece_color));
		final ListPreference opponentsCheckersPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_opponents_checkers_piece_color));
		final ListPreference playersChessPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_players_chess_piece_color));
		final ListPreference opponentsChessPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_opponents_chess_piece_color));

		opponentsCheckersPieceColor.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onOpponentCheckersPieceColorPreferenceChange(playersCheckersPieceColor, newValue);
			}
		});

		playersCheckersPieceColor.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onPlayerCheckersPieceColorPreferenceChange(opponentsCheckersPieceColor, newValue);
			}
		});

		opponentsChessPieceColor.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onOpponentChessPieceColorPreferenceChange(playersChessPieceColor, newValue);
			}
		});

		playersChessPieceColor.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onPlayerChessPieceColorPreferenceChange(opponentsChessPieceColor, newValue);
			}
		});
	}


	@Override
	public void onAttach(final Activity activity)
	{
		super.onAttach(activity);
		listeners = (GameSettingsFragmentListeners) activity;
	}


}
