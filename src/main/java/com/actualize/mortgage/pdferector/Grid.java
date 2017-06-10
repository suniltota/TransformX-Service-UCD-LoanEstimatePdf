package com.actualize.mortgage.pdferector;

public class Grid extends Drawable {
	public static final float Dynamic = -1f;
	
	public enum Direction { Vertical, Horizontal }
	
	Drawable[][] cells = null;
	Border[][] topBorders = null, leftBorders = null;
	float heights[] = null;
	float widths[] = null;
	
	public Grid(int nrows, float heights[], int ncolumns, float widths[]) {
		// Create grid contents
		cells = new Drawable[nrows][ncolumns];

		// Create grid borders
		topBorders = new Border[nrows+1][ncolumns];
		leftBorders = new Border[nrows][ncolumns+1];

		// Determine row heights
		this.heights = new float[nrows];
		for (int i = 0; i < nrows; i++)
			this.heights[i] = (i < heights.length) ? heights[i] : heights[heights.length-1];

		// Determine column widths
		this.widths  = new float[ncolumns];
		for (int i = 0; i < ncolumns; i++)
			this.widths[i] = (i < widths.length) ? widths[i] : widths[widths.length-1];
	}

	public int rows() {
		return heights.length;
	}
	
	public int columns() {
		return widths.length;
	}
	
	public float height(Page page, float width) throws Exception {
		float height = 0;
		for (int i = 0; i < rows(); i++)
			height += rowHeight(page, i);
		return height;
	}

	public float width(Page page) throws Exception {
		float width = 0;
		for (int i = 0; i < columns(); i++)
			width += widths[i];
		return width;
	}
	
	private float rowHeight(Page page, int row) throws Exception {
		if (heights[row] != Dynamic)
			return heights[row];
		float height = 0;
		for (int i = 0; i < columns(); i++) {
			if (cells[row][i] != null) {
				float h = cells[row][i].height(page, widths[i]);
				if (h > height)
					height = h;
			}
		}
		return height;
	}

	public void draw(Page page, float x, float y, float width) throws Exception {
		// Draw cell backgrounds
		float cellY = y + height(page, width);
		for (int row = 0; row < rows(); row++) {
			float height = rowHeight(page, row);
			float cellX = x;
			cellY -= height;
			for (int col = 0; col < columns(); col++) {
				if (height > 0 && cells[row][col] != null)
					cells[row][col].drawBackground(page, cellX, cellY, widths[col], height);
				cellX += widths[col];
			}
		}

		// Draw vertical borders (e.g. left or right or column)
		cellY = y + height(page, width);
		for (int row = 0; row < rows(); row++) {
			float height = rowHeight(page, row);
			float cellX = x;
			cellY -= height;
			for (int col = 0; col < columns() + 1; col++) {
				if (height > 0 && leftBorders[row][col] != null)
					leftBorders[row][col].draw(page, cellX, cellY, cellX, cellY + height);
				if (col < columns()) cellX += widths[col];
			}
		}
		
		// Draw horizontal borders (e.g. top or bottom or row)
		cellY = y + height(page, width);
		for (int row = 0; row < rows() + 1; row++) {
			float height = row == 0 ? 0 : rowHeight(page, row-1);
			float cellX = x;
			cellY -= height;
			for (int col = 0; col < columns(); col++) {
				if (height > 0 && topBorders[row][col] != null)
					topBorders[row][col].draw(page, cellX, cellY, cellX + widths[col], cellY);
				cellX += widths[col];
			}
		}

		// Draw cell foregrounds
		cellY = y + height(page, width);
		for (int row = 0; row < rows(); row++) {
			float height = rowHeight(page, row);
			float cellX = x;
			cellY -= height;
			for (int col = 0; col < columns(); col++) {
				if (height > 0 && cells[row][col] != null)
					cells[row][col].draw(page, cellX, cellY, widths[col], height);
				cellX += widths[col];
			}
		}
	}
	
	public Drawable setCell(int row, int col, Drawable drawable) {
		try {
			cells[row][col] = drawable;
			return drawable;
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Array index (" + row + "," + col + ") is out of Bounds" + e);
			throw e;
		}
	}
	
	public Drawable getCell(int row, int col) {
		try {
			return cells[row][col];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Array index (" + row + "," + col + ") is out of Bounds" + e);
			throw e;
		}
	}
	
	public Drawable setBorder(Alignment.Horizontal border, int row, int col, Border style) {
		if (border == Alignment.Horizontal.CENTER)
			return this;
		if (border == Alignment.Horizontal.RIGHT)
			col++;
		leftBorders[row][col] = style;
		return this;
	}
	
	public Drawable setBorder(Alignment.Horizontal border, int col, Border style) {
		// Set all vertical borders for the given column (adjust for right)
		if (border == Alignment.Horizontal.CENTER)
			return this;
		if (border == Alignment.Horizontal.RIGHT)
			col++;
		for (int row = 0; row < rows(); row++)
			leftBorders[row][col] = style;
		return this;
	}
	
	public Drawable setBorder(Alignment.Vertical border, int row, int col, Border style) {
		if (border == Alignment.Vertical.MIDDLE)
			return this;
		if (border == Alignment.Vertical.BOTTOM)
			row++;
		topBorders[row][col] = style;
		return this;
	}
	
	public Drawable setBorder(Alignment.Vertical border, int row, Border style) {
		// Set all vertical borders for the given row (adjust for bottom)
		if (border == Alignment.Vertical.MIDDLE)
			return this;
		if (border == Alignment.Vertical.BOTTOM)
			row++;
		for (int col = 0; col < columns(); col++)
			topBorders[row][col] = style;
		return this;
	}
}