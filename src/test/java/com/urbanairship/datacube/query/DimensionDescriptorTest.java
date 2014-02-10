package com.urbanairship.datacube.query;

import com.google.common.reflect.TypeToken;
import com.urbanairship.datacube.BucketType;
import com.urbanairship.datacube.Dimension;
import com.urbanairship.datacube.DimensionAndBucketType;
import com.urbanairship.datacube.bucketers.StringToBytesBucketer;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class DimensionDescriptorTest {

    @Test
    public void testDimensionalTypeTokenFromAnon() {
        Dimension<String> dim = new Dimension<String>("hello", new StringToBytesBucketer(), false, 7){};
        DimensionAndBucketType dimensionAndBucketType = new DimensionAndBucketType(dim);
        TypeToken<?> typeToken = dimensionAndBucketType.dimension.getTypeToken().get();
        Assert.assertTrue(typeToken.isAssignableFrom(String.class));
    }

    @Test
    public void testDimTypeTokenExplicit() {
        Dimension<String> dim = new Dimension<String>("hello", new StringToBytesBucketer(), false, 7, String.class);
        DimensionAndBucketType dimensionAndBucketType = new DimensionAndBucketType(dim);
        TypeToken<?> typeToken = dimensionAndBucketType.dimension.getTypeToken().get();
        Assert.assertTrue(typeToken.isAssignableFrom(String.class));
    }
}
