package com.charlesmadere.android.classygames.settings;


import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.SpannableString;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class GameSettingsFragment extends PreferenceFragment
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
		 * This method is called whenever the user changes the player's
		 * checkers piece color.
		 * 
		 * @param opponentsCheckersPieceColor
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
		public boolean onPlayersCheckersPieceColorPreferenceChange(final ListPreference opponentsCheckersPieceColor,
			final Object newValue);


		/**
		 * This method is called whenever the user changes the opponent's
		 * checkers piece color.
		 * 
		 * @param playersCheckersPieceColor
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
		public boolean onOpponentsCheckersPieceColorPreferenceChange(final ListPreference playersCheckersPieceColor,
			final Object newValue);


		/**
		 *
		 *
		 * @param opponentsChessPieceColor
		 *
		 *
		 * @param newValue
		 *
		 *
		 * @return
		 *
		 */
		public boolean onPlayersChessPieceColorPreferenceChange(final ListPreference opponentsChessPieceColor,
			final Object newValue);


		/**
		 *
		 *
		 * @param playersChessPieceColor
		 *
		 *
		 * @param newValue
		 *
		 *
		 * @return
		 *
		 */
		public boolean onOpponentsChessPieceColorPreferenceChange(final ListPreference playersChessPieceColor,
			final Object newValue);


	}




	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final String actionBarTitle = getString(R.string.game_settings);
		final SpannableString styledActionBarTitle = Utilities.makeStyledString(getActivity().getAssets(),
			actionBarTitle, TypefaceUtilities.BLUE_HIGHWAY_D);
		getActivity().getActionBar().setTitle(styledActionBarTitle);

		addPreferencesFromResource(R.xml.settings_game);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Utilities.setBackground(getActivity(), getView());

		final ListPreference playersCheckersPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_players_checkers_piece_color));
		final ListPreference opponentsCheckersPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_opponents_checkers_piece_color));
		final ListPreference playersChessPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_players_chess_piece_color));
		final ListPreference opponentsChessPieceColor = (ListPreference) findPreference(getString(R.string.settings_key_opponents_chess_piece_color));

		playersCheckersPieceColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onPlayersCheckersPieceColorPreferenceChange(opponentsCheckersPieceColor, newValue);
			}
		});

		opponentsCheckersPieceColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onOpponentsCheckersPieceColorPreferenceChange(playersCheckersPieceColor, newValue);
			}
		});

		playersChessPieceColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onPlayersChessPieceColorPreferenceChange(opponentsChessPieceColor, newValue);
			}
		});

		opponentsChessPieceColor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(final Preference preference, final Object newValue)
			{
				return listeners.onOpponentsChessPieceColorPreferenceChange(playersChessPieceColor, newValue);
			}
		});
	}


	@Override
	public void onAttach(final Activity activity)
	{
		super.onAttach(activity);

		try
		{
			listeners = (GameSettingsFragmentListeners) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


}
