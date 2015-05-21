package com.arrested.lbmmo.ws.controller;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.arrested.lbmmo.persistence.repository.QuestInProgressRepository;

public class MapControllerTest {

	@Mock
	private QuestInProgressRepository qipRepo;
	
	@InjectMocks
	private MapController controller;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
}
