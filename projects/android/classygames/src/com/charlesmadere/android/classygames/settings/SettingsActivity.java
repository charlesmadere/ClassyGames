package com.charlesmadere.android.classygames.settings;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.Utilities;

import java.util.List;


/**
 * Some of the code used in making this class and it's corresponding XML files
 * was taken from the official Android Documentation.
 * https://developer.android.com/guide/topics/ui/settings.html
 */
public final class SettingsActivity extends SherlockPreferenceActivity implements
	GameSettingsFragment.GameSettingsFragmentListeners
{


	private ListPreference checkersPieceColorOpponents;
	private ListPreference checkersPieceColorPlayers;
	private ListPreference chessPieceColorOpponents;
	private ListPreference chessPieceColorPlayers;




	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(final Bundle savedInstanceState)
	// The addPreferencesFromResource() methods below are causing some
	// deprecation warnings. In this case, the fact that they're here is fine.
	// They have to be used if the running version of Android is below
	// Honeycomb (v3.0). Same situation with the findPreference methods. See
	// more information about API levels here:
	// https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels
	{
		super.onCreate(savedInstanceState);
		Utilities.setActionBar(this, R.string.settings, true);
		getListView().setBackgroundResource(R.drawable.bg_bright);
		getListView().setCacheColorHint(getResources().getColor(R.color.cache_color_hint));

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		// Check to see if the running version of Android is below Honeycomb.
		{
			// get the intent's action
			final String action = getIntent().getAction();

			if (Utilities.verifyValidString(action))
			{
				if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_game)))
				// the intent's action is saying that we need to show the game
				// settings preference file
				{
					addPreferencesFromResource(R.xml.settings_game);
					Utilities.setActionBar(this, R.string.game_settings, true);

					checkersPieceColorOpponents = (ListPreference) findPreference(getString(R.string.settings_key_opponents_checkers_piece_color));
					checkersPieceColorPlayers = (ListPreference) findPreference(getString(R.string.settings_key_players_checkers_piece_color));
					chessPieceColorOpponents = (ListPreference) findPreference(getString(R.string.settings_key_opponents_chess_piece_color));
					chessPieceColorPlayers = (ListPreference) findPreference(getString(R.string.settings_key_players_chess_piece_color));

					checkersPieceColorOpponents.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
					{
						@Override
						public boolean onPreferenceChange(final Preference preference, final Object newValue)
						{
							if (checkersPieceColorPlayers == null)
							{
								checkersPieceColorPlayers = (ListPreference) findPreference(getString(R.string.settings_key_players_checkers_piece_color));
							}

							return onOpponentCheckersPieceColorPreferenceChange(checkersPieceColorPlayers, newValue);
						}
					});

					checkersPieceColorPlayers.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
					{
						@Override
						public boolean onPreferenceChange(final Preference preference, final Object newValue)
						{
							if (checkersPieceColorOpponents == null)
							{
								checkersPieceColorOpponents = (ListPreference) findPreference(getString(R.string.settings_key_opponents_checkers_piece_color));
							}

							return onPlayerCheckersPieceColorPreferenceChange(checkersPieceColorOpponents, newValue);
						}
					});

					chessPieceColorOpponents.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
					{
						@Override
						public boolean onPreferenceChange(final Preference preference, final Object newValue)
						{
							if (chessPieceColorPlayers == null)
							{
								chessPieceColorPlayers = (ListPreference) findPreference(getString(R.string.settings_key_opponents_chess_piece_color));
							}

							return onPlayerChessPieceColorPreferenceChange(chessPieceColorPlayers, newValue);
						}
					});

					chessPieceColorPlayers.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
					{
						@Override
						public boolean onPreferenceChange(final Preference preference, final Object newValue)
						{
							if (chessPieceColorOpponents == null)
							{
								chessPieceColorOpponents = (ListPreference) findPreference(getString(R.string.settings_key_players_chess_piece_color));
							}

							return onPlayerChessPieceColorPreferenceChange(chessPieceColorOpponents, newValue);
						}
					});
				}
				else if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_notifications)))
				// the intent's action is saying that we need to show the
				// miscellaneous settings preference file
				{
					addPreferencesFromResource(R.xml.settings_notification);
					Utilities.setActionBar(this, R.string.notification_settings, true);
				}
				else if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_register)))
				// the intent's action is saying that we need to show the
				// RegisterForNotificationsActivity
				{
					startActivity(new Intent(this, RegisterForNotificationsActivity.class));
				}
				else if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_unregister)))
				// the intent's action is saying that we need to show the
				// UnregisterFromNotificationsActivity
				{
					startActivity(new Intent(this, UnregisterFromNotificationsActivity.class));
				}
				else if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_about)))
				// the intent's action is saying that we need to show the
				// AboutActivity
				{
					addPreferencesFromResource(R.xml.settings_about);
					Utilities.setActionBar(this, R.string.about, true);
				}
				else
				// The intent's action was something strange. We'll show the
				// default preference file. This else case shouldn't ever
				// happen.
				{
					addPreferencesFromResource(R.xml.settings_headers_legacy);
				}
			}
			else
			// For Android devices running any version below Honeycomb.
			{
				addPreferencesFromResource(R.xml.settings_headers_legacy);
			}
		}
	}


	@Override
	public void onBuildHeaders(final List<Header> target)
	// Called only when this Android device is running Honeycomb and above.
	{
		super.onBuildHeaders(target);
		loadHeadersFromResource(R.xml.settings_headers, target);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}




	/**
	 * Checks the values on both of the inputs to see if they're equal. This is
	 * to be used whenever a user changes a value in a ListPreference. We don't
	 * want the user to set the same value for both of the ListPreferences.
	 * This will return true if both values are the same.
	 *
	 * @param otherTeamPreference
	 * A reference to the ListPreference object that was NOT just now modified.
	 *
	 * @param newTeamValue
	 * The new value for the ListPreference object that the user just now
	 * changed.
	 *
	 * @return
	 * Returns false if both of the given values are equal.
	 */
	private boolean makeSureBothTeamsArentTheSameColor(final ListPreference otherTeamPreference, final Object newTeamValue)
	{
		final String newlySetColor = (String) newTeamValue;
		final String otherTeamColor = otherTeamPreference.getValue();

		if (newlySetColor.equalsIgnoreCase(otherTeamColor))
		{
			Utilities.easyToast(this, R.string.make_sure_that_you_dont_set_both_teams_color_to_the_same_thing_your_changes_to_this_setting_have_not_been_saved);
			return false;
		}
		else
		{
			return true;
		}
	}




	@Override
	public boolean onOpponentCheckersPieceColorPreferenceChange(final ListPreference playerCheckersPieceColor, final Object newValue)
	{
		return makeSureBothTeamsArentTheSameColor(playerCheckersPieceColor, newValue);
	}


	@Override
	public boolean onPlayerCheckersPieceColorPreferenceChange(final ListPreference opponentCheckersPieceColor, final Object newValue)
	{
		return makeSureBothTeamsArentTheSameColor(opponentCheckersPieceColor, newValue);
	}


	@Override
	public boolean onOpponentChessPieceColorPreferenceChange(final ListPreference playerChessPieceColor, final Object newValue)
	{
		return makeSureBothTeamsArentTheSameColor(playerChessPieceColor, newValue);
	}


	@Override
	public boolean onPlayerChessPieceColorPreferenceChange(final ListPreference opponentChessPieceColor, final Object newValue)
	{
		return makeSureBothTeamsArentTheSameColor(opponentChessPieceColor, newValue);
	}


}
