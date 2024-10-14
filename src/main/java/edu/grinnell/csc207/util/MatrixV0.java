package edu.grinnell.csc207.util;

/**
 * An implementation of two-dimensional matrices.
 *
 * @author David William Stroud
 * @author Samuel A. Rebelsky
 *
 * @param <T>
 *   The type of values stored in the matrix.
 */
public class MatrixV0<T> implements Matrix<T> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * This is the array that contains all the elements of the matrix.
   */
  T[] backing;
  /**
   * This is the width of the matrix.
   */
  int width;
  /**
   * This is the height of the matrix.
   */
  int height;
  /**
   * This is the default value passed to the constructor.
   */
  T defaultValue;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new matrix of the specified width and height with the
   * given value as the default.
   *
   * @param width1
   *   The width of the matrix.
   * @param height1
   *   The height of the matrix.
   * @param def
   *   The default value, used to fill all the cells.
   *
   * @throws NegativeArraySizeException
   *   If either the width or height are negative.
   */
  public MatrixV0(int width1, int height1, T def) {
    this(width1, height1);
    this.defaultValue = def;
    for (int i = 0; i < this.height(); i++) {
      for (int j = 0; j < this.width(); j++) {
        this.set(i, j, def);
      } // for
    } // for
  } // MatrixV0(int, int, T)

  /**
   * Create a new matrix of the specified width and height with
   * null as the default value.
   *
   * @param width1
   *   The width of the matrix.
   * @param height1
   *   The height of the matrix.
   *
   * @throws NegativeArraySizeException
   *   If either the width or height are negative.
   */
  @SuppressWarnings({ "unchecked" })
  public MatrixV0(int width1, int height1) {
    if (width1 < 0) {
      throw new NegativeArraySizeException("Negative width given to MatrixV0 constructor");
    } // if
    if (height1 < 0) {
      throw new NegativeArraySizeException("Negative height given to MatrixV0 constructor");
    } // if

    this.width = width1;
    this.height = height1;
    this.backing = (T[]) new Object[this.width() * this.height()];
  } // MatrixV0

  // +--------------+------------------------------------------------
  // | Core methods |
  // +--------------+

  /**
   * Get the element at the given row and column.
   *
   * @param row
   *   The row of the element.
   * @param col
   *   The column of the element.
   *
   * @return the value at the specified location.
   *
   * @throws IndexOutOfBoundsException
   *   If either the row or column is out of reasonable bounds.
   */
  public T get(int row, int col) {
    return this.backing[this.getIndex(row, col)];
  } // get(int, int)

  /**
   * Set the element at the given row and column.
   *
   * @param row
   *   The row of the element.
   * @param col
   *   The column of the element.
   * @param val
   *   The value to set.
   *
   * @throws IndexOutOfBoundsException
   *   If either the row or column is out of reasonable bounds.
   */
  public void set(int row, int col, T val) {
    this.backing[this.getIndex(row, col)] = val;
  } // set(int, int, T)

  /**
   * Determine the number of rows in the matrix.
   *
   * @return the number of rows.
   */
  public int height() {
    return this.height;
  } // height()

  /**
   * Determine the number of columns in the matrix.
   *
   * @return the number of columns.
   */
  public int width() {
    return this.width;
  } // width()

  /**
   * Insert a row filled with the default value.
   *
   * @param row
   *   The number of the row to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the row is negative or greater than the height.
   */
  public void insertRow(int row) {
    this.insertRowUnchecked(row, this.defaultRun(this.width()));
  } // insertRow(int)

  /**
   * Insert a row filled with the specified values.
   *
   * @param row
   *   The number of the row to insert.
   * @param vals
   *   The values to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the row is negative or greater than the height.
   * @throws ArraySizeException
   *   If the size of vals is not the same as the width of the matrix.
   */
  public void insertRow(int row, T[] vals) throws ArraySizeException {
    if (vals.length != this.width()) {
      throw new ArraySizeException(
              "Array of length "
                      + vals.length
                      + " not appropriate for Matrix of width "
                      + this.width()
      );
    } // if

    this.insertRowUnchecked(row, vals);
  } // insertRow(int, T[])

  /**
   * Insert a column filled with the default value.
   *
   * @param col
   *   The number of the column to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the column is negative or greater than the width.
   */
  public void insertCol(int col) {
    this.insertColUnchecked(col, this.defaultRun(this.height()));
  } // insertCol(int)

  /**
   * Insert a column filled with the specified values.
   *
   * @param col
   *   The number of the column to insert.
   * @param vals
   *   The values to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the column is negative or greater than the width.
   * @throws ArraySizeException
   *   If the size of vals is not the same as the height of the matrix.
   */
  public void insertCol(int col, T[] vals) throws ArraySizeException {
    if (vals.length != this.height()) {
      throw new ArraySizeException(
              "Array of length "
                      + vals.length
                      + " is not appropriate for Matrix of height "
                      + this.height()
      );
    } // if

    this.insertColUnchecked(col, vals);
  } // insertCol(int, T[])

  /**
   * Delete a row.
   *
   * @param row
   *   The number of the row to delete.
   *
   * @throws IndexOutOfBoundsException
   *   If the row is negative or greater than or equal to the height.
   */
  @SuppressWarnings({ "unchecked" })
  public void deleteRow(int row) {
    if (row < 0 || row >= this.height()) {
      throw new IndexOutOfBoundsException(
              "Row index "
                      + row
                      + " is not valid for Matrix of height "
                      + this.height()
      );
    } // if

    T[] newBacking = (T[]) new Object[this.backing.length - this.width()];

    int numBefore = row * this.width();
    System.arraycopy(
            this.backing,
            0,
            newBacking,
            0,
            numBefore
    );
    System.arraycopy(
            this.backing,
            numBefore + this.width(),
            newBacking,
            numBefore,
            newBacking.length - numBefore
    );

    this.backing = newBacking;
    this.height--;
  } // deleteRow(int)

  /**
   * Delete a column.
   *
   * @param col
   *   The number of the column to delete.
   *
   * @throws IndexOutOfBoundsException
   *   If the column is negative or greater than or equal to the width.
   */
  @SuppressWarnings({ "unchecked" })
  public void deleteCol(int col) {
    if (col < 0 || col >= this.width()) {
      throw new IndexOutOfBoundsException(
              "Column index "
                      + col
                      + " not appropriate for Matrix of width "
                      + this.width()
      );
    } // if

    T[] newBacking = (T[]) new Object[this.backing.length - this.height()];

    for (int i = 0; i < this.height(); i++) {
      System.arraycopy(
              this.backing,
              i * this.width(),
              newBacking,
              i * this.width() - i,
              col
      );
      System.arraycopy(
              this.backing,
              i * this.width() + col + 1,
              newBacking,
              i * this.width() - i + col,
              this.width() - col - 1
      );
    } // for

    this.backing = newBacking;
    this.width--;
  } // deleteCol(int)

  /**
   * Fill a rectangular region of the matrix.
   *
   * @param startRow
   *   The top edge / row to start with (inclusive).
   * @param startCol
   *   The left edge / column to start with (inclusive).
   * @param endRow
   *   The bottom edge / row to stop with (exclusive).
   * @param endCol
   *   The right edge / column to stop with (exclusive).
   * @param val
   *   The value to store.
   *
   * @throws IndexOutOfBoundsException
   *   If the rows or columns are inappropriate.
   */
  public void fillRegion(int startRow, int startCol, int endRow, int endCol,
      T val) {
    if (startRow >= this.height() || startRow < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "Start row index"
              + startRow
              + " is not appropriate for matrix of height "
              + this.height()
      );
    } // if
    if (startCol >= this.width() || startCol < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "Start column index"
                      + startCol
                      + " is not appropriate for matrix of width "
                      + this.width()
      );
    } // if
    if (endRow > this.height() || endRow < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "End row index "
              + endRow
              + " is not appropriate for matrix of height "
              + this.height()
      );
    } // if
    if (endCol > this.width() || endCol < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "End column index "
              + endCol
              + " is not appropriate for matrix of width "
              + this.width()
      );
    } // if

    for (int i = startRow; i < endRow; i++) {
      for (int j = startCol; j < endCol; j++) {
        this.set(i, j, val);
      } // for
    } // for
  } // fillRegion(int, int, int, int, T)

  /**
   * Fill a line (horizontal, vertical, diagonal).
   *
   * @param startRow
   *   The row to start with (inclusive).
   * @param startCol
   *   The column to start with (inclusive).
   * @param deltaRow
   *   How much to change the row in each step.
   * @param deltaCol
   *   How much to change the column in each step.
   * @param endRow
   *   The row to stop with (exclusive).
   * @param endCol
   *   The column to stop with (exclusive).
   * @param val
   *   The value to store.
   *
   * @throws IndexOutOfBoundsException
   *   If the rows or columns are inappropriate.
   */
  public void fillLine(int startRow, int startCol, int deltaRow, int deltaCol,
      int endRow, int endCol, T val) {
    if (endRow > this.height() || endRow < -1) {
      throw new ArrayIndexOutOfBoundsException(
              "Row index "
                      + endRow
                      + " is not appropriate for matrix of height "
                      + this.height()
      );
    } // if
    if (endCol > this.width() || endCol < -1) {
      throw new ArrayIndexOutOfBoundsException(
              "Column index "
                      + endCol
                      + " is not appropriate for matrix of width "
                      + this.width()
      );
    } // if
    if (deltaCol * (endCol - startCol) < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "End column "
              + endCol
              + " is before start column "
              + startCol
      );
    } // if
    if (deltaRow * (endRow - startRow) < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "End row "
              + endRow
              + " is before start row "
              + startRow
      );
    } // if

    for (int i = startRow, j = startCol;
         this.isWithin(startRow, endRow, i)
            && this.isWithin(startCol, endCol, j);
         i += deltaRow, j += deltaCol) {
      this.set(i, j, val);
    } // for
  } // fillLine(int, int, int, int, int, int, T)

  /**
   * A make a copy of the matrix. May share references (e.g., if individual
   * elements are mutable, mutating them in one matrix may affect the other
   * matrix) or may not.
   *
   * @return a copy of the matrix.
   */
  public Matrix<T> clone() {
    MatrixV0<T> cloned = new MatrixV0<>(this.width(), this.height(), this.defaultValue);
    for (int i = 0; i < this.height(); i++) {
      for (int j = 0; j < this.width(); j++) {
        cloned.set(i, j, this.get(i, j));
      } // for
    } // for
    return cloned;
  } // clone()

  /**
   * Determine if this object is equal to another object.
   *
   * @param other
   *   The object to compare.
   *
   * @return true if the other object is a matrix with the same width,
   * height, and equal elements; false otherwise.
   */
  public boolean equals(Object other) {
    return other instanceof Matrix && this.equals((Matrix) other);
  } // equals(Object)

  /**
   * Determine if this Matrix is equal to another Matrix.
   * @param other The Matrix to compare.
   * @return true if the other matrix has the same width, height, and elements; false otherwise.
   */
  public boolean equals(Matrix other) {
    if (other.width() != this.width() || other.height() != this.height()) {
      return false;
    } // if

    for (int i = 0; i < this.height(); i++) {
      for (int j = 0; j < this.width(); j++) {
        T thisVal = this.get(i, j);
        Object otherVal = other.get(i, j);
        if (thisVal == null) {
          if (otherVal != null) {
            return false;
          } // if
        } else if (!thisVal.equals(otherVal)) {
          return false;
        } // if
      } // for
    } // for

    return true;
  } // equals(Matrix)

  /**
   * Compute a hash code for this matrix. Included because any object
   * that implements `equals` is expected to implement `hashCode` and
   * ensure that the hash codes for two equal objects are the same.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int multiplier = 7;
    int code = this.width() + multiplier * this.height();
    for (int row = 0; row < this.height(); row++) {
      for (int col = 0; col < this.width(); col++) {
        T val = this.get(row, col);
        if (val != null) {
          // It's okay if the following computation overflows, since
          // it will overflow uniformly.
          code = code * multiplier + val.hashCode();
        } // if
      } // for col
    } // for row
    return code;
  } // hashCode()

  /**
   * This method calculates the index into the backing array
   * required to access the index (row, col).
   *
   * @param row The row wanted.
   * @param col The column wanted.
   * @return The index of the backing array needed to access (row, col).
   *
   * @throws ArrayIndexOutOfBoundsException
   *   This exception is thrown if either the row or column are
   *   invalid for this array at the current size.
   */
  int getIndex(int row, int col) {
    if (row >= this.height() || row < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "Row index "
                      + row
                      + " not valid for Matrix of height "
                      + this.height()
      );
    } // if
    if (col >= this.width() || col < 0) {
      throw new ArrayIndexOutOfBoundsException(
              "Column index "
                      + col
                      + " not valid for Matrix of width "
                      + this.width()
      );
    } // if

    return row * this.width() + col;
  } // getIndex(int, int)

  /**
   * This method creates an array of length len
   * with every index set to the default value of this matrix.
   *
   * @param len The length of the array to create.
   *
   * @return An array with the default value of this matrix in every cell.
   */
  @SuppressWarnings({ "unchecked" })
  T[] defaultRun(int len) {
    T[] arr = (T[]) new Object[len];
    for (int i = 0; i < len; i++) {
      arr[i] = this.defaultValue;
    } // for
    return arr;
  } // defaultRun(int)

  /**
   * Inserts a row into this matrix at the specified position and fills it
   * with the specified values. For a call to this method to be valid,
   * vals must have a length equal to the width of the matrix.
   * @param row The position at which to insert this row.
   * @param vals The values to use when filling the inserted row.
   *
   * @throws IndexOutOfBoundsException This is thrown if the row index is not valid for this matrix.
   */
  @SuppressWarnings({ "unchecked" })
  void insertRowUnchecked(int row, T[] vals) {
    if (row < 0 || row > this.height()) {
      throw new ArrayIndexOutOfBoundsException(
              "Row index "
                      + row
                      + " not valid for Matrix of height "
                      + this.height()
      );
    } // if

    T[] newBacking = (T[]) new Object[this.backing.length + this.width()];

    int numBefore = row == this.height() ? this.backing.length : this.getIndex(row, 0);

    System.arraycopy(
            this.backing,
            0,
            newBacking,
            0,
            numBefore
    );
    System.arraycopy(
            this.backing,
            numBefore,
            newBacking,
            numBefore + this.width(),
            newBacking.length - numBefore - this.width()
    );

    this.backing = newBacking;
    this.height++;

    for (int i = 0; i < this.width(); i++) {
      this.set(row, i, vals[i]);
    } // for
  } // insertRowUnchecked(int, T[])

  /**
   * This method inserts a column into the array at position col.
   * It fills the inserted column with values from vals.
   * Calls to this method are only valid if the length of vals is
   * equal to the height of the matrix.
   * @param col The position at which to insert the column.
   * @param vals The values with which to fill the column.
   *
   * @throws IndexOutOfBoundsException
   *   This is thrown if the column index is
   *   not valid for this matrix.
   */
  @SuppressWarnings({ "unchecked" })
  void insertColUnchecked(int col, T[] vals) {
    if (col < 0 || col > this.width()) {
      throw new IndexOutOfBoundsException(
              "Column index "
                      + col
                      + " is not appropriate for Matrix of width "
                      + this.width()
      );
    } // if

    T[] newBacking = (T[]) new Object[this.backing.length + this.height()];

    for (int i = 0; i < this.height(); i++) {
      System.arraycopy(
              this.backing,
              i * this.width(),
              newBacking,
              i * this.width() + i,
              col
      );
      System.arraycopy(
              this.backing,
              i * this.width() + col,
              newBacking,
              i * this.width() + i + col + 1,
              this.width() - col
      );
    } // for

    this.backing = newBacking;
    this.width++;

    for (int i = 0; i < this.height(); i++) {
      this.set(i, col, vals[i]);
    } // for
  } // insertColUnchecked(int, T[])

  /**
   * This method calculates whether an index is inside a [start, end) range.
   * @param start The beginning of the range.
   * @param end The end of the range.
   * @param index The index being tested.
   * @return Whether the index is in the range.
   */
  boolean isWithin(int start, int end, int index) {
    if (start < end) {
      return start <= index && index < end;
    } else {
      return end < index && index <= start;
    } // if-else
  } // isWithin(int, int, int)
} // class MatrixV0
