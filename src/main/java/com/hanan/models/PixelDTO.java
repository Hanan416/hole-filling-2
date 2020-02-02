package com.hanan.models;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PixelDTO {

    private int xCoordinate;
    private int yCoordinate;
    private double value;


    public PixelDTO(int xCoordinate, int yCoordinate, double value) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.value = value;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PixelDTO))
            return false;

        PixelDTO pixelDTO = (PixelDTO) o;
        return pixelDTO.xCoordinate == this.xCoordinate &&
                pixelDTO.yCoordinate == this.yCoordinate &&
                pixelDTO.value == this.value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(xCoordinate)
                .append(yCoordinate)
                .append(value)
                .toHashCode();
    }
}
