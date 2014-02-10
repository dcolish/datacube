package com.urbanairship.datacube.query;

import com.google.common.base.Optional;
import com.google.common.reflect.TypeToken;
import com.urbanairship.datacube.BucketType;
import com.urbanairship.datacube.Dimension;
import com.urbanairship.datacube.DimensionAndBucketType;

/**
 * Similar to a {@link com.google.common.reflect.TypeToken} because in our infinite wisdom,
 * we only capture wildcards in the Rollup. This will certainly irk me to no end.
 *
 * Use it like this
 * <pre>
 *     Dimenstion<String> dim = new Dimension("StringDim", new StringToBytesBucketer(), false, 7);
 *     DimensionAndBucketType dimBucketAndType = new DimensionAndBucketType(dim);
 *     DimensionDescriptor<String> dimDesc = new DimensionDescriptor(dimBucketAndType){};
 * </pre>
 */
public class DimensionDescriptor {
    private final DimensionAndBucketType dimensionAndBucketType;

    public DimensionDescriptor(DimensionAndBucketType dimensionAndBucketType) {
        this.dimensionAndBucketType = dimensionAndBucketType;
    }

    public static DimensionDescriptor wrap(DimensionAndBucketType dimensionAndBucketType) {
        return new DimensionDescriptor(dimensionAndBucketType);
    }

    public Dimension<?> getDimension() {
        return dimensionAndBucketType.dimension;
    }

    public BucketType getBucketType() {
        return dimensionAndBucketType.bucketType;
    }

    public <C> boolean isAssignableFrom(C coordinate) {
        Optional<? extends TypeToken<?>> typeToken = dimensionAndBucketType.dimension.getTypeToken();
        return typeToken.isPresent() && typeToken.get().isAssignableFrom(coordinate.getClass());
    }
}



