/*
Copyright 2012 Urban Airship and Contributors
*/

package com.urbanairship.datacube;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.urbanairship.datacube.query.DimensionDescriptor;
import com.urbanairship.datacube.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Use this class to describe a rollup that you want the datacube to keep.
 * <p/>
 * For example, if you're counting events with the dimensions (color, size, flavor) and you
 * want to keep a total count for all (color, size) combinations, you'd specify that using a Rollup.
 */
public class Rollup {
    private final List<DimensionAndBucketType> components;
    private final ImmutableList<DimensionDescriptor> descriptors;

    public Rollup(Dimension<?>... dims) {
        this.components = new ArrayList<DimensionAndBucketType>(dims.length);
        for (Dimension<?> dim : dims) {
            this.components.add(new DimensionAndBucketType(dim, BucketType.IDENTITY));
        }
        this.descriptors = buildDescriptors(this.components);
    }

    public Rollup(Set<DimensionAndBucketType> components) {
        this.components = new ArrayList<DimensionAndBucketType>(components); // defensive copy
        this.descriptors = buildDescriptors(components);
    }

    private ImmutableList<DimensionDescriptor> buildDescriptors(Collection<DimensionAndBucketType> components) {
        ImmutableList.Builder<DimensionDescriptor> builder = ImmutableList.builder();
        for (DimensionAndBucketType component : components) {
            builder.add(DimensionDescriptor.wrap(component));
        }
        return builder.build();
    }

    /**
     * Convenient wrapper around {@link #Rollup(Set)} that builds a set for you.
     */
    public Rollup(Dimension<?> d) {
        this(ImmutableSet.of(new DimensionAndBucketType(d, BucketType.IDENTITY)));
    }

    /**
     * Convenient wrapper around {@link #Rollup(Set)} that builds a set for you.
     */
    public Rollup(Dimension<?> d, BucketType bt) {
        this(ImmutableSet.of(new DimensionAndBucketType(d, bt)));
    }

    /**
     * Convenient wrapper around {@link #Rollup(Set)} that builds a set for you.
     */
    public Rollup(Dimension<?> d1, BucketType bt1, Dimension<?> d2, BucketType bt2) {
        this(ImmutableSet.of(new DimensionAndBucketType(d1, bt1),
                new DimensionAndBucketType(d2, bt2)));
    }

    /**
     * Convenient wrapper around {@link #Rollup(Set)} that builds a set for you.
     */
    public Rollup(Dimension<?> d1, BucketType bt1, Dimension<?> d2, BucketType bt2,
                  Dimension<?> d3, BucketType bt3) {
        this(ImmutableSet.of(new DimensionAndBucketType(d1, bt1),
                new DimensionAndBucketType(d2, bt2), new DimensionAndBucketType(d3, bt3)));
    }

    /**
     * Convenient wrapper around {@link #Rollup(Set)} that builds a set for you.
     */
    public Rollup(Dimension<?> d1, Dimension<?> d2) {
        this(ImmutableSet.of(new DimensionAndBucketType(d1, BucketType.IDENTITY),
                new DimensionAndBucketType(d2, BucketType.IDENTITY)));
    }

    /**
     * Convenient wrapper around {@link #Rollup(Set)} that builds a set for you.
     */
    public Rollup(Dimension<?> d1, Dimension<?> d2, BucketType bt2) {
        this(ImmutableSet.of(new DimensionAndBucketType(d1, BucketType.IDENTITY),
                new DimensionAndBucketType(d2, bt2)));
    }

    List<DimensionAndBucketType> getComponents() {
        return components;
    }

    public ImmutableList<DimensionDescriptor> getDescriptors() {
        return descriptors;
    }

    public QueryBuilder newQueryBuilder() {
        return QueryBuilder.newQuery(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(Rollup over ");
        sb.append(components);
        sb.append(")");
        return sb.toString();
    }
}
