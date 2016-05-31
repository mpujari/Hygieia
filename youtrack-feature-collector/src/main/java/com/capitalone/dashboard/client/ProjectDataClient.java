package com.capitalone.dashboard.client;

import static com.capitalone.dashboard.util.YouTrackConstants.YOUTRACK;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capitalone.dashboard.json.ProjectInfo;
import com.capitalone.dashboard.model.Scope;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.rest.client.YouTrackRestApi;

public class ProjectDataClient extends BaseClient {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ScopeRepository projectRepo;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final YouTrackRestApi youTrackRestApi;

	public ProjectDataClient(ScopeRepository projectRepository, FeatureCollectorRepository featureCollectorRepository,
			YouTrackRestApi youTrackRestApi) {
		logger.debug("Constructing data collection for the feature widget, story-level data...");
		this.projectRepo = projectRepository;
		this.featureCollectorRepository = featureCollectorRepository;
		this.youTrackRestApi = youTrackRestApi;
	}

	public void updateProjectInformation() {
		try {
			updateMongoInfo(youTrackRestApi.getTeams());
		} catch (Exception e) {
			logger.error("Error in collecting Youtrack Data: " + e.getMessage(), e);
		}
	}

	private void updateMongoInfo(List<ProjectInfo> list) {
		for (ProjectInfo projectInfo : list) {
			Scope scope = new Scope();
			String id = projectInfo.getShortName();
			/*
			 * Removing any existing entities where they exist in the local DB
			 * store...
			 */
			this.removeExistingEntity(id);
			// collectorId
			scope.setCollectorId(featureCollectorRepository.findByName(YOUTRACK).getId());
			// ID;
			scope.setpId(id);
			// name;
			scope.setName(projectInfo.getName());
			// beginDate - does not exist for youtrack
			scope.setBeginDate("");
			// endDate - does not exist for youtrack
			scope.setEndDate("");
			// changeDate - does not exist for youtrack
			scope.setChangeDate("");
			// assetState - does not exist for youtrack
			scope.setAssetState("Active");
			// isDeleted - does not exist for youtrack
			scope.setIsDeleted("False");
			// path - does not exist for youtrack
			scope.setProjectPath(projectInfo.getName());
			// Saving back to MongoDB
			projectRepo.save(scope);
		}
	}

	protected boolean removeExistingEntity(String localId) {
		boolean deleted = false;

		try {
			ObjectId tempEntId = projectRepo.getScopeIdById(localId).get(0).getId();
			if (localId.equalsIgnoreCase(projectRepo.getScopeIdById(localId).get(0).getpId())) {
				projectRepo.delete(tempEntId);
				deleted = true;
			}
		} catch (IndexOutOfBoundsException ioobe) {
			logger.debug("Nothing matched the redundancy checking from the database", ioobe);
		} catch (Exception e) {
			logger.error("There was a problem validating the redundancy of the data model", e);
		}
		return deleted;
	}

}
