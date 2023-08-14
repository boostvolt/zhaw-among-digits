package ch.zhaw.pm2.amongdigits.utils.schema;

class SchemaManager implements Schema {

  private final byte unsetValue;
  private final byte minimumValue;
  private final byte maximumValue;
  private final int width;
  private final int blockWidth;
  private final int totalFields;
  private final int blockCount;
  private final int bitMask;

  SchemaManager(
      final byte unsetValue,
      final byte minimumValue,
      final byte maximumValue,
      final int width,
      final int blockWidth) {
    if (minimumValue <= unsetValue && unsetValue <= maximumValue) {
      throw new IllegalArgumentException(
          "Maximum value must be greater than unset value and unset value must be greater than minimum value");
    }
    this.unsetValue = unsetValue;

    if (maximumValue - minimumValue + 1 != width) {
      throw new IllegalArgumentException(
          "Maximum value minus minimum value plus one must be equal to width");
    }
    this.minimumValue = minimumValue;
    this.maximumValue = maximumValue;

    if (width != blockWidth * blockWidth) {
      throw new IllegalArgumentException("Width must be equal to block width squared");
    }
    if (width <= 0) {
      throw new IllegalArgumentException("Width must be greater than zero");
    }
    if (width % blockWidth != 0) {
      throw new IllegalArgumentException("Width must be a multiple of block width");
    }
    this.width = width;
    this.blockWidth = blockWidth;
    this.totalFields = width * width;
    this.blockCount = width / blockWidth;

    int bitMaskCounter = 0;
    for (int i = minimumValue; i <= maximumValue; i++) {
      bitMaskCounter |= 1 << i;
    }
    this.bitMask = bitMaskCounter;
  }

  /** {@inheritDoc} */
  @Override
  public byte getMinimumValue() {
    return minimumValue;
  }

  /** {@inheritDoc} */
  @Override
  public byte getMaximumValue() {
    return maximumValue;
  }

  /** {@inheritDoc} */
  @Override
  public byte getUnsetValue() {
    return unsetValue;
  }

  /** {@inheritDoc} */
  @Override
  public int getWidth() {
    return width;
  }

  /** {@inheritDoc} */
  @Override
  public int getBlockWidth() {
    return blockWidth;
  }

  /** {@inheritDoc} */
  @Override
  public int getTotalFields() {
    return totalFields;
  }

  /** {@inheritDoc} */
  @Override
  public int getBlockCount() {
    return blockCount;
  }

  /** {@inheritDoc} */
  @Override
  public int getBitMask() {
    return bitMask;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isValueValid(final byte value) {
    return value == unsetValue || (value >= minimumValue && value <= maximumValue);
  }

  @Override
  public boolean isBitMaskValid(final int bitMask) {
    return (bitMask & (~this.bitMask)) == 0;
  }

  @Override
  public boolean areCoordsValid(final int row, final int column) {
    return row >= 0 && row < width && column >= 0 && column < width;
  }
}
