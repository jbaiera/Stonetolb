package com.stonetolb.asset.graphics;

import static com.stonetolb.util.Floatation.closeEnough;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.stonetolb.resource.system.SystemContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TextureTest {

	private static Texture TEXTURE;
	private static Texture EVEN_TEXTURE;

	@Mock
	private static SystemContext MOCK_CONTEXT;

	@Before
	public void setUp()
	throws Exception
	{
		MockitoAnnotations.initMocks(this);

		TEXTURE = Texture.builder(1, 2)
				.setImageHeight(100)
				.setImageWidth(100)
				.setTextureHeight(128)
				.setTextureWidth(128)
				.setSystemContext(MOCK_CONTEXT)
				.build();

		EVEN_TEXTURE = Texture.builder(3, 4)
				.setImageHeight(64)
				.setImageWidth(64)
				.setTextureHeight(64)
				.setTextureWidth(64)
				.setSystemContext(MOCK_CONTEXT)
				.build();
	}

	@Test
	public void testBind()
	throws Exception
	{
		TEXTURE.bind();
		verify(MOCK_CONTEXT, times(1)).bindTexture(eq(1), eq(2));
	}

	@Test
	public void testGetSubTexture()
	throws Exception
	{
		Texture subTexture = TEXTURE.getSubTexture(50, 50, 25, 25);

		subTexture.bind();
		verify(MOCK_CONTEXT, times(1)).bindTexture(eq(1), eq(2));

		assertEquals(subTexture.getImageHeight(), 25);
		assertEquals(subTexture.getImageWidth(), 25);
	}

	@Test
	public void testGetImageHeight()
	throws Exception
	{
		assertEquals(TEXTURE.getImageHeight(), 100);
	}

	@Test
	public void testGetImageWidth()
	throws Exception
	{
		assertEquals(TEXTURE.getImageWidth(), 100);
	}

	@Test
	public void testGetHeight()
	throws Exception
	{
		assertTrue(closeEnough(EVEN_TEXTURE.getHeight(), 1.0f));
	}

	@Test
	public void testGetWidth()
	throws Exception
	{
		assertTrue(closeEnough(EVEN_TEXTURE.getWidth(), 1.0f));
	}

	@Test
	public void testGetXOrigin()
	throws Exception
	{
		assertTrue(closeEnough(EVEN_TEXTURE.getXOrigin(), 0.0f));
	}

	@Test
	public void testGetYOrigin()
	throws Exception
	{
		assertTrue(closeEnough(EVEN_TEXTURE.getYOrigin(), 0.0f));
	}
}
