package com.urbanairship.datacube.query;

import com.google.common.collect.Lists;
import com.urbanairship.datacube.Address;
import com.urbanairship.datacube.DataCube;
import com.urbanairship.datacube.Dimension;
import com.urbanairship.datacube.Rollup;
import com.urbanairship.datacube.bucketers.StringToBytesBucketer;
import com.urbanairship.datacube.ops.LongOp;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class QueryBuilderTest {
    private final Dimension<String> name = new Dimension<String>("name", new StringToBytesBucketer(), false, 7){};
    private final Dimension<String> tag = new Dimension<String>("tag", new StringToBytesBucketer(), false, 7){};
    private final List<Dimension<?>> dimensions = Lists.newArrayList();
    private DataCube<LongOp> cube;
    private Rollup rollup;

    @Before
    public void setUp() {
        dimensions.add(name);
        dimensions.add(tag);
        rollup = new Rollup(name, tag);
        cube = new DataCube<LongOp>(dimensions, Lists.newArrayList(rollup));
    }

    @Test
    public void testSimpleQuery() throws Exception {
        QueryBuilder builder = rollup.newQueryBuilder();
        builder.setDimension("name", "Frank").setDimension("tag", "donuts");
        Address address = builder.buildAddress(cube);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testUnknownDimension() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        QueryBuilder queryBuilder = rollup.newQueryBuilder();
        queryBuilder.setDimension("ham", "eggs");
    }
}
