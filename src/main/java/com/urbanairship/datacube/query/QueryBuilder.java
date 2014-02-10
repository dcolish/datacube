package com.urbanairship.datacube.query;


import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.urbanairship.datacube.*;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides a generic API for defining and building dimensional queries.
 *
 *
 */
@NotThreadSafe
public class QueryBuilder {

    private final ImmutableMap<String, DimensionDescriptor> dimensionDescriptors;
    private final Map<DimensionDescriptor, Object> coordinateMap = Maps.newHashMap();

    private QueryBuilder(ImmutableList<DimensionDescriptor> descriptors) {
        ImmutableMap.Builder<String, DimensionDescriptor> builder = ImmutableMap.builder();
        for (DimensionDescriptor descriptor : descriptors) {
            builder.put(descriptor.getDimension().getName(), descriptor);
        }
        dimensionDescriptors = builder.build();
    }

    //TODO:dc: this should really not be a builder at this point but we fucked that up too
    public static QueryBuilder newQuery(Rollup rollup) {
        return new QueryBuilder(rollup.getDescriptors());
    }

    public <T> QueryBuilder setDimension(String dimensionName, T coordinate) {
        DimensionDescriptor descriptor = dimensionDescriptors.get(dimensionName);
        Preconditions.checkArgument(descriptor != null, "No dimension found for name, %s", dimensionName);

        //NOTE:dc: the above precondition will fail and prevent null here
        //noinspection ConstantConditions
        Preconditions.checkArgument(descriptor.isAssignableFrom(coordinate), "Unassignable Dimension for coordinate " +
                "type, " +
                "%s, %s", coordinate.getClass().getCanonicalName(), descriptor.getClass().getCanonicalName());
        coordinateMap.put(descriptor, coordinate);
        return this;
    }

    public ImmutableCollection<DimensionDescriptor> getDimensionDescriptors() {
        return dimensionDescriptors.values();
    }

    private static Function<DimensionDescriptor, Dimension<?>> EXTRACT_DIMENSION = new Function<DimensionDescriptor, Dimension<?>>() {
        @Nullable
        @Override
        public Dimension<?> apply(@Nullable DimensionDescriptor input) {
            Preconditions.checkNotNull(input);
            //noinspection ConstantConditions
            return input.getDimension();
        }
    };

    public Address buildAddress(DataCube<?> cube) {
        Set<Dimension<?>> dimensions = Sets.newHashSet(cube.getDimensions());

        ImmutableCollection<DimensionDescriptor> descriptors = dimensionDescriptors.values();

        boolean dimensionsMatch = dimensions.containsAll(Collections2.transform(descriptors, EXTRACT_DIMENSION));

        Preconditions.checkArgument(dimensionsMatch,
                "Cube dimensions do not match DimensionDescriptors, cube=%s, builder=%s",
                dimensions, dimensionDescriptors);

        Preconditions.checkState(coordinateMap.keySet().containsAll(descriptors),
                "Incomplete descriptor set, missing %", Sets.difference(coordinateMap.keySet(),
                Sets.newHashSet(descriptors)));

        final ReadBuilder readBuilder = new ReadBuilder(cube);
        for (DimensionDescriptor dimensionDescriptor : descriptors) {
            readBuilder.at(dimensionDescriptor.getDimension(), dimensionDescriptor.getBucketType(),
                    coordinateMap.get(dimensionDescriptor));
        }
        return readBuilder.build();
    }
}
