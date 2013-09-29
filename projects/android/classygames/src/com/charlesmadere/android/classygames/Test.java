package com.charlesmadere.android.classygames;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.charlesmadere.android.classygames.models.games.Coordinate;
import com.charlesmadere.android.classygames.views.BoardView;
import com.charlesmadere.android.classygames.views.PositionView;


public class Test extends Activity
{


	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		final BoardView boardView = (BoardView) findViewById(R.id.test_board);
		boardView.setPositionsOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final PositionView positionView = (PositionView) v;
				final Coordinate coordinate = positionView.getCoordinate();
				Toast.makeText(Test.this, coordinate.toString(), Toast.LENGTH_SHORT);
			}
		});
	}


}
