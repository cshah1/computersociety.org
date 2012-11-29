package org.computer.portal.permission;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.Role;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.util.portlet.PortletProps;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class StartupAction extends SimpleAction {

	@Override
	public void run(String[] ids) throws ActionException {
		System.out.println("Algo6 Startup Action is running....");
//		if(_log.isDebugEnabled()) {
//			_log.debug("Algo6 Startup Action is running....");
//		}
		String runnable = "false";
		try {
			runnable = PrefsPropsUtil.getString("company.startup.algo6.event.run");
			if(!Boolean.valueOf(runnable)) {
				System.out.println("Checking the property at portlet level...");
				runnable = PortletProps.get("company.startup.algo6.event.run");
			}
		} catch (SystemException e) {
			// do nothing
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(Boolean.valueOf(runnable)){
			System.out.println("Algo6 Startup Action is runnable....");
			try {
				
				List<Role> usersRoles = findByDescriptionAndName("Auto", "Regular ");
				List<Role> groupsRoles = findByDescriptionAndName("Auto", "Community ");
				
				List existingUsersRoles = new ArrayList(usersRoles.size());
				List existingGroupsRoles = new ArrayList(groupsRoles.size());
				
				for(Role userRole : usersRoles) {
					existingUsersRoles.add(userRole.getRoleId());
				}
				
				for(Role groupRole : groupsRoles) {
					existingGroupsRoles.add(groupRole.getRoleId());
				}
				
				if(usersRoles != null && groupsRoles != null) {
					
					// create new replacement roles
					if(createRoles()) {
					
						// update Users_Roles and Groups_Roles for all the entities
						// Group Entity
						List<ResourcePermission> resourcePermissions = findByNameScopeActionIds(StartupAction.GROUP_CLASSNAME, StartupAction.SCOPE, StartupAction.GROUP_ACTIONID);
						updateResourcePermissions(ENTITY.GROUP);
						updateResourcePermissions(ENTITY.GROUPGROUP);
						updateUsersGroupsRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						// DLFile Entity
						resourcePermissions = findByNameScopeActionIds(StartupAction.DLFILE_CLASSNAME, StartupAction.SCOPE, StartupAction.DLFILE_ACTIONID);
						updateResourcePermissions(ENTITY.DLFILE);
						updateUsersDLFileRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						// DLFOLDER
						resourcePermissions = findByNameScopeActionIds(StartupAction.DLFOLDER_CLASSNAME, StartupAction.SCOPE, StartupAction.DLFOLDER_ACTIONID);
						updateResourcePermissions(ENTITY.DLFOLER);
						updateUsersDLFolderRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						// IGFOLDER
						resourcePermissions = findByNameScopeActionIds(StartupAction.IGFOLDER_CLASSNAME, StartupAction.SCOPE, StartupAction.IGFOLDER_ACTIONID);
						updateResourcePermissions(ENTITY.IGFOLDER);
						updateUsersIGFolderRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						// JC
						resourcePermissions = findByNameScopeActionIds(StartupAction.JC_CLASSNAME, StartupAction.SCOPE, StartupAction.JC_ACTIONID);
						updateResourcePermissions(ENTITY.JC);
						updateUsersJCRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						// MBCATEGORY
						resourcePermissions = findByNameScopeActionIds(StartupAction.MBCATEGORY_CLASSNAME, StartupAction.SCOPE, StartupAction.MBCATEGORY_ACTIONID);
						updateResourcePermissions(ENTITY.MBCAT);
						updateUsersMBCatRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						//WIKINODE
						resourcePermissions = findByNameScopeActionIds(StartupAction.WIKINODE_CLASSNAME, StartupAction.SCOPE, StartupAction.WIKINODE_ACTIONID);
						updateResourcePermissions(ENTITY.WIKINODE);
						updateUsersWIKINodeRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						//WIKIPAGE
						resourcePermissions = findByNameScopeActionIds(StartupAction.WIKIPAGE_CLASSNAME, StartupAction.SCOPE, StartupAction.WIKIPAGE_OWNER_ACTIONID);
						updateResourcePermissions(ENTITY.WIKIPAGE_OWNER);
						updateUsersWIKIPageOwnerRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						//WIKIPAGE EDITOR
						resourcePermissions = findByNameScopeActionIds(StartupAction.WIKIPAGE_CLASSNAME, StartupAction.SCOPE, StartupAction.WIKIPAGE_EDITOR_ACTIONID);
						updateResourcePermissions(ENTITY.WIKIPAGE_EDITOR);
						updateUsersWIKIPageEditorRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						//CALEVENT
						resourcePermissions = findByNameScopeActionIds(StartupAction.CALEVENT_CLASSNAME, StartupAction.SCOPE, StartupAction.CALEVENT_ACTIONID);
						updateResourcePermissions(ENTITY.CALEVENT);
						updateUsersCalEventsRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						//ASSETTAG
						resourcePermissions = findByNameScopeActionIds(StartupAction.ASSETTAG_CLASSNAME, StartupAction.SCOPE, StartupAction.ASSETTAG_ACTIONID);
						updateResourcePermissions(ENTITY.ASSETTAG);
						updateResourcePermissions(ENTITY.ASSETTAG_GROUP);
						updateAssetRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						resourcePermissions = findByNameScopeActionIds(StartupAction.ASSETTAG_CLASSNAME, StartupAction.SCOPE, StartupAction.ASSETTAG_EDITOR_ACTIONID);
						updateResourcePermissions(ENTITY.ASSETAG_EDITOR);
						updateAssetEditorRoles(resourcePermissions, existingUsersRoles, existingGroupsRoles);
						
						// now update ResourcePermissions
//						updateResourcePermissions();
						
						// finally delete the roles
//						deleteRolesInRole();
						
						System.out.println("Algo6 Startup Action is complete....");
						_log.debug("Algo6 Startup Action is complete....");
					}
				}
				
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			if(_log.isDebugEnabled()) {
				_log.debug("Algo6 Hook is not configured to be runnable.");
			}
			System.out.println("Algo6 Hook is not configured to be runnable.");
		}
		if(_log.isDebugEnabled()) {
			_log.debug("Algo6 Startup Action is finished.");
		}
		_log.error("Algo6 Startup Action is finished.");
	}
	
	public boolean createRoles() {
		DB db = DBFactoryUtil.getDB();

		try {
			db.runSQL(GROUP_ROLE_INSERT_SQL);
			db.runSQL(GROUP_GROUPROLE_INSERT_SQL);
			db.runSQL(FILE_ROLE_INSERT_SQL);
			db.runSQL(FOLDER_ROLE_INSERT_SQL);
			db.runSQL(IGFOLDER_ROLE_INSERT_SQL);
			db.runSQL(JOURNAL_ARTICLE_ROLE_INSERT_SQL);
			db.runSQL(MBCAT_ROLE_INSERT_SQL);
			db.runSQL(WIKI_NODE_ROLE_INSERT_SQL);
			db.runSQL(WIKI_PAGE_OWNER_ROLE_INSERT_SQL);
			db.runSQL(WIKI_PAGE_EDITOR_ROLE_INSERT_SQL);
			db.runSQL(BOOKMARKS_ROLE_INSERT_SQL);
			db.runSQL(CAL_EVENT_ROLE_INSERT_SQL);
			db.runSQL(ASSETTAG_OWNER_ROLE_INSERT_SQL);
			db.runSQL(ASSETTAG_GROUP_ROLE_INSERT_SQL);
			db.runSQL(ASSETTAG_EDITOR_ROLE_INSERT_SQL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		
		return true;
	}
	
	public void updateUsersGroupsRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("Group Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.GROUP_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.GROUP_GROUPOWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersDLFileRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("DLFILE Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.FILE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.FILE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersDLFolderRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("DLFolder Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.FOLER_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.FOLER_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersIGFolderRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("IGFOLDER Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.IGFOLDER_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.IGFOLDER_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersJCRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("JOURNAL Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.JOURNAL_ARTICLE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.JOURNAL_ARTICLE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersMBCatRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("MBCAT Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.MBCAT_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.MBCAT_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersWIKINodeRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("WIKI Node Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.WIKI_NODE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.WIKI_NODE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersWIKIPageEditorRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("WIKI Page Editor Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.WIKI_PAGE_EDITOR_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.WIKI_PAGE_EDITOR_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersWIKIPageOwnerRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("WIKI Page Owner Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.WIKI_PAGE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.WIKI_PAGE_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersBookmarksRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("Bookmarks Editor Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.BOOKMARKS_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.BOOKMARKS_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateUsersCalEventsRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("Cal Events Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.CAL_EVENT_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.CAL_EVENT_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateAssetRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("Assets Owner Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.ASSETTAG_OWNER_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
				updateGroupsRoles(StartupAction.ASSETTAG_GROUP_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
	public void updateAssetEditorRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException, PortalException, SystemException {
		for(ResourcePermission resourcePermission : resourcePermissions) {
			System.out.println("Assets Editor Resource Permission: " + resourcePermission.getRoleId());
			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
				updateUsersRoles(StartupAction.ASSETTAG_EDITOR_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
//				updateGroupsRoles(StartupAction.ASSETTAG_GROUP_ROLE_ID, resourcePermission.getRoleId());
				// now remove this role from the Role_ table
//				RoleLocalServiceUtil.deleteRole(resourcePermission.getRoleId());
			}
		}
	}
	
//	public void updateUsersAssetGroupRoles(List<ResourcePermission> resourcePermissions, List existingUsersRoles, List existingGroupsRoles) throws IOException, SQLException {
//		for(ResourcePermission resourcePermission : resourcePermissions) {
//			System.out.println("Assets Group Owner Resource Permission: " + resourcePermission.getRoleId());
//			if(existingUsersRoles.contains(resourcePermission.getRoleId())) {
//				updateUsersRoles(StartupAction.ASSETTAG_GROUP_ROLE_ID, resourcePermission.getRoleId());
//			} else if(existingGroupsRoles.contains(resourcePermission.getRoleId())) {
//				updateGroupsRoles(StartupAction.ASSETTAG_GROUP_ROLE_ID, resourcePermission.getRoleId());
//			}
//		}
//	}
	
	public boolean updateUsersRoles(int newRoleId, long existingRoleId) throws IOException, SQLException {
		DB db = DBFactoryUtil.getDB();
		
		String sql = UPDATE_USERS_ROLES.replace("<new_roleId>", String.valueOf(newRoleId)).replace("<existing_roleId>", String.valueOf(existingRoleId));
		System.out.println("Updating USERS_ROLES: " + sql);
		try {
			db.runSQL(sql);
			
		} catch (IOException e) {
			throw e;
			
		} catch (SQLException e) {
			// remove the duplicate user role
			if(e instanceof com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException && e.getMessage().startsWith("Duplicate entry")) {
				sql = DELETE_ROLES_FROM_USERS_ROLES + existingRoleId;
				System.out.println("Deleting USERS_ROLES: " + sql);
				try {
					db.runSQL(sql);
					
				} catch (IOException e1) {
					throw e1;
					
				} catch (SQLException e1) {
					System.err.println("Error deleting from USERS_ROLES.");
				}
			}
			else {
				throw e;
			}
			
		}
		
		return true;
	}
	
	public boolean updateGroupsRoles(int newRoleId, long existingRoleId) throws IOException, SQLException {
		DB db = DBFactoryUtil.getDB();
		
		String sql = UPDATE_GROUPS_ROLES.replace("<new_roleId>", String.valueOf(newRoleId)).replace("<existing_roleId>", String.valueOf(existingRoleId));
		System.out.println("Updating GROUPS_ROLES: " + sql);
		try {
			db.runSQL(sql);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (SQLException e) {
			// remove the duplicate user role
			if(e instanceof com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException && e.getMessage().startsWith("Duplicate entry")) {
				sql = DELETE_ROLES_FROM_GROUPS_ROLES + existingRoleId;
				System.out.println("Deleting GROUPS_ROLES: " + sql);
				try {
					db.runSQL(sql);
					
				} catch (IOException e1) {
					throw e1;
					
				} catch (SQLException e1) {
					System.err.println("Error deleting from GROUPS_ROLES.");
				}
			}
			else {
				throw e;
			}
		}
		
		return true;
	}
	
	public enum ENTITY { GROUP, GROUPGROUP, DLFILE, DLFOLER, IGFOLDER, JC, MBCAT, WIKINODE, WIKIPAGE_OWNER, WIKIPAGE_EDITOR, BOOKMARKS, CALEVENT, ASSETTAG, ASSETTAG_GROUP, ASSETAG_EDITOR };
	public void updateResourcePermissions(ENTITY entity) throws IOException, SQLException {
		DB db = DBFactoryUtil.getDB();
		
		System.out.println("Updating ResourcePermissions.... ");
		String sql = "";
		try {
			
			switch(entity) {
				case GROUP:
					sql = UPDATE_GROUP_SQL + WHERE_GROUP_SQL;
					System.out.println("Update Groups: " + sql);
					db.runSQL(sql);
					break;
				case GROUPGROUP:
					sql = UPDATE_GROUP_GROUPROLE_SQL + WHERE_GROUP__GROUPROLE_SQL;
					System.out.println("Update Group's Group: " + sql);
					db.runSQL(sql);
					break;
				case DLFILE:
					sql = UPDATE_DLFILE_SQL + WHERE_DLFILE_SQL;
					System.out.println("Update DLFile: " + sql);
					db.runSQL(sql);
					break;
				case DLFOLER:
					sql = UPDATE_DLFOLER_SQL + WHERE_DLFOLDER_SQL;
					System.out.println("Update DLFolder: " + sql);
					db.runSQL(sql);
					break;
				case IGFOLDER:
					sql = UPDATE_IGFOLER_SQL + WHERE_IGFOLDER_SQL;
					System.out.println("Update IGFolder: " + sql);
					db.runSQL(sql);
					break;
				case JC:
					sql = UPDATE_JC_SQL + WHERE_JC_SQL;
					System.out.println("Update JC: " + sql);
					db.runSQL(sql);
					break;
				case MBCAT:
					sql = UPDATE_MBCATEGORY_SQL + WHERE_MBCATEGORY_SQL;
					System.out.println("Update MBCategory: " + sql);
					db.runSQL(sql);
					break;
				case WIKINODE:
					sql = UPDATE_WIKINODE_SQL + WHERE_WIKINODE_SQL;
					System.out.println("Update WIKI Node: " + sql);
					db.runSQL(sql);
					break;
				case WIKIPAGE_OWNER:
					sql = UPDATE_WIKIPAGE_OWNER_SQL + WHERE_WIKIPAGE_OWNER_SQL;
					System.out.println("Update WIKI Page Owner: " + sql);
					db.runSQL(sql);
					break;
				case WIKIPAGE_EDITOR:
					sql = UPDATE_WIKIPAGE_EDITOR_SQL + WHERE_WIKIPAGE_EDITOR_SQL;
					System.out.println("Update WIKI Page Editor: " + sql);
					db.runSQL(sql);
					break;
				case CALEVENT:
					sql = UPDATE_CALEVENT_SQL + WHERE_CALEVENT_SQL;
					System.out.println("Update CAL Event: " + sql);
					db.runSQL(sql);
					break;
				case BOOKMARKS:
					sql = UPDATE_BOOKMRKS_SQL + WHERE_BOOKMARKS_SQL;
					System.out.println("Update CAL Event: " + sql);
					db.runSQL(sql);
					break;
				case ASSETTAG:
					sql = UPDATE_ASSETTAG_SQL + WHERE_ASSETTAG_SQL;
					System.out.println("Update ASSETTAG Owner: " + sql);
					db.runSQL(sql); 
					break;
				case ASSETTAG_GROUP:
					sql = UPDATE_ASSETTAG_GROUP_SQL + WHERE_ASSETTAG_GROUP_SQL;
					System.out.println("Update ASSETTAG Group Owner: " + sql);
					db.runSQL(sql);
					break;
				case ASSETAG_EDITOR:
					sql = UPDATE_ASSETTAG_EDITOR_SQL + WHERE_ASSETTAG_EDITOR_SQL;
					System.out.println("Update ASSETTAG Editor: " + sql);
					db.runSQL(sql);
					break;
			}
			
		} catch (IOException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public boolean replaceRolesInResourcePermission() {
		return true;
	}
	
	public boolean replaceRolesInUsersRoles() {
		return true;
	}
	
	public boolean deleteRolesInRole() throws IOException, SQLException {
		
		
		DB db = DBFactoryUtil.getDB();
		
		
		System.out.println("Delete ROLES: " + DELETE_ROLES);
		try {
			db.runSQL(DELETE_ROLES);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		
		return true;
		
	}
	
	public List<ResourcePermission> findByNameScopeActionIds(String name, int scope, int actionIds) throws SystemException {
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ResourcePermission.class, PortalClassLoaderUtil.getClassLoader());
		 
		Criterion criterion = null;
		 
		criterion = RestrictionsFactoryUtil.eq("name", name);
		 
		criterion = RestrictionsFactoryUtil.and(criterion, RestrictionsFactoryUtil.eq("scope",scope));
		 
		criterion = RestrictionsFactoryUtil.and(criterion , RestrictionsFactoryUtil.eq("actionIds", (long)actionIds));
		 
		dynamicQuery.add(criterion);
		
		List<ResourcePermission> resourcePermissions = ResourcePermissionLocalServiceUtil.dynamicQuery(dynamicQuery);
		return resourcePermissions;
    }

	/**
	 * @param description value: 'Auto%'
	 * @param name value: 'Regular %' or value: 'Community %'
	 * @return
	 * @throws SystemException
	 */
	public List<Role> findByDescriptionAndName(String description, String name) throws SystemException {
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Role.class, PortalClassLoaderUtil.getClassLoader());
		 
		Criterion criterion = null;
		 
		criterion = RestrictionsFactoryUtil.like("description", description+ StringPool.PERCENT);
		criterion = RestrictionsFactoryUtil.and(criterion, RestrictionsFactoryUtil.like("name", name + StringPool.PERCENT)); 
		
		 
		dynamicQuery.add(criterion);
		
		List<Role> roles = RoleLocalServiceUtil.dynamicQuery(dynamicQuery);
		return roles;
		
	}
	

	
	
	private static int GROUP_OWNER_ROLE_ID = 5965921; 
	private static String GROUP_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values ("+
													GROUP_OWNER_ROLE_ID + ", 10092, 10030, " + GROUP_OWNER_ROLE_ID +", 'Group Owner', 'Replacement Role', 1)";
	
	private static int GROUP_GROUPOWNER_ROLE_ID = 5965934; 
	private static String GROUP_GROUPROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values ("+
			GROUP_GROUPOWNER_ROLE_ID + ", 10092, 10030, " + GROUP_GROUPOWNER_ROLE_ID +", 'Group Group Owner', 'Replacement Role', 2)";
	
	private static int FILE_OWNER_ROLE_ID = 5965922;
	private static String FILE_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" +
	                                              FILE_OWNER_ROLE_ID+", 10092, 10030, " + FILE_OWNER_ROLE_ID + ", 'File Owner', 'Replacement Role', 1)";
	
	private static int FOLER_OWNER_ROLE_ID = 5965923;
	private static String FOLDER_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" +
	                                                  FOLER_OWNER_ROLE_ID + ", 10092, 10030, " + FOLER_OWNER_ROLE_ID + ", 'Folder Owner', 'Replacement Role', 1)";
	 
	private static int IGFOLDER_OWNER_ROLE_ID = 5965924;
	private static String IGFOLDER_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
														IGFOLDER_OWNER_ROLE_ID + ", 10092, 10030, " + IGFOLDER_OWNER_ROLE_ID + ", 'IGFolder Owner', 'Replacement Role', 1)";
	
	private static int JOURNAL_ARTICLE_OWNER_ROLE_ID = 5965925;
	private static String JOURNAL_ARTICLE_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
																JOURNAL_ARTICLE_OWNER_ROLE_ID + ", 10092, 10030, " + JOURNAL_ARTICLE_OWNER_ROLE_ID + ", 'Journal Content Owner', 'Replacement Role', 1)";
	
	private static int MBCAT_OWNER_ROLE_ID = 5965926;
	private static String MBCAT_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
													MBCAT_OWNER_ROLE_ID + ", 10092, 10030, " + MBCAT_OWNER_ROLE_ID + ", 'MBCategory Owner', 'Replacement Role', 1)";
	
	private static int WIKI_NODE_OWNER_ROLE_ID = 5965927;
	private static String WIKI_NODE_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" +
														WIKI_NODE_OWNER_ROLE_ID + ", 10092, 10030, " + WIKI_NODE_OWNER_ROLE_ID + ", 'WIKI Node Owner', 'Replacement Role', 1)";
	
	private static int WIKI_PAGE_OWNER_ROLE_ID = 5965928;
	private static String WIKI_PAGE_OWNER_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
																WIKI_PAGE_OWNER_ROLE_ID + ", 10092, 10030, " + WIKI_PAGE_OWNER_ROLE_ID +", 'WIKI Page Owner', 'Replacement Role', 1)";
	
	private static int WIKI_PAGE_EDITOR_ROLE_ID = 5965929;
	private static String WIKI_PAGE_EDITOR_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
																WIKI_PAGE_EDITOR_ROLE_ID + ", 10092, 10030, " + WIKI_PAGE_EDITOR_ROLE_ID + ", 'WIKI Page Editor', 'Replacement Role', 1)";
	
	private static int BOOKMARKS_OWNER_ROLE_ID = 5965930;
	private static String BOOKMARKS_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
														BOOKMARKS_OWNER_ROLE_ID + ", 10092, 10030, " + BOOKMARKS_OWNER_ROLE_ID + ", 'Bookmarks Folder Owner', 'Replacement Role', 1)";
	
	private static int CAL_EVENT_OWNER_ROLE_ID = 5965931;
	private static String CAL_EVENT_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
															CAL_EVENT_OWNER_ROLE_ID + ", 10092, 10030, " + CAL_EVENT_OWNER_ROLE_ID +", 'Cal Event Owner', 'Replacement Role', 1)";
	
	private static int ASSETTAG_OWNER_ROLE_ID = 5965932;
	private static String ASSETTAG_OWNER_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
															ASSETTAG_OWNER_ROLE_ID + ", 10092, 10030, " + ASSETTAG_OWNER_ROLE_ID +", 'ASSET Tag Owner', 'Replacement Role', 1)";
	private static int ASSETTAG_GROUP_ROLE_ID = 5965933;
	private static String ASSETTAG_GROUP_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
			ASSETTAG_GROUP_ROLE_ID + ", 10092, 10030, " + ASSETTAG_GROUP_ROLE_ID +", 'ASSET Tag Group Owner', 'Replacement Group Role', 2)";
	
	private static int ASSETTAG_EDITOR_ROLE_ID = 5965935;
	private static String ASSETTAG_EDITOR_ROLE_INSERT_SQL = "insert into Role_ (roleId, companyId, classNameId, classPK, name, description, type_) values (" + 
			ASSETTAG_EDITOR_ROLE_ID + ", 10092, 10030, " + ASSETTAG_EDITOR_ROLE_ID +", 'ASSET Tag Editor', 'Replacement Role', 1)";
	
	
	/**
	 * SQL statements to update ResourcePermission table
	 */
	
	private static String SELECT_RP_SQL = "select ResourcePermission from ResourcePermission_bak";
	private static int SCOPE = 4;
	private static String description="Auto";
	
	private static String UPDATE_GROUP_SQL = "update ResourcePermission set roleId=" + StartupAction.GROUP_OWNER_ROLE_ID;
	private static String WHERE_GROUP_SQL= " where name='com.liferay.portal.model.Group' and scope=4 and actionIds=18724 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	private static String GROUP_CLASSNAME = "com.liferay.portal.model.Group";
	private static int GROUP_ACTIONID = 18724;
	
	private static String UPDATE_GROUP_GROUPROLE_SQL = "update ResourcePermission set roleId=" + StartupAction.GROUP_GROUPOWNER_ROLE_ID;
	private static String WHERE_GROUP__GROUPROLE_SQL= " where name='com.liferay.portal.model.Group' and scope=4 and actionIds=18724 and roleId in " +
			"(select roleId from Role_ where description like 'Auto%' and name like 'Community %')";
	
	private static int DLFILE_ACTIONID = 55;
	private static String DLFILE_CLASSNAME = "com.liferay.portlet.documentlibrary.model.DLFileEntry";
	private static String UPDATE_DLFILE_SQL = "update ResourcePermission set roleId=" + StartupAction.FILE_OWNER_ROLE_ID;
	private static String WHERE_DLFILE_SQL = " where name='com.liferay.portlet.documentlibrary.model.DLFileEntry' and scope=4 and actionIds=55 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int DLFOLDER_ACTIONID = 253;
	private static String DLFOLDER_CLASSNAME = "com.liferay.portlet.documentlibrary.model.DLFolder";
	private static String UPDATE_DLFOLER_SQL = "update ResourcePermission set roleId=" + StartupAction.FOLER_OWNER_ROLE_ID;
	private static String WHERE_DLFOLDER_SQL=" where name='com.liferay.portlet.documentlibrary.model.DLFolder' and scope=4 and actionIds=253 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int IGFOLDER_ACTIONID = 125;
	private static String IGFOLDER_CLASSNAME = "com.liferay.portlet.imagegallery.model.IGFolder";
	private static String UPDATE_IGFOLER_SQL = "update ResourcePermission set roleId=" + StartupAction.IGFOLDER_OWNER_ROLE_ID;
	private static String WHERE_IGFOLDER_SQL=" where name='com.liferay.portlet.imagegallery.model.IGFolder' and scope=4 and actionIds=125 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    JC_ACTIONID = 119;
	private static String JC_CLASSNAME = "com.liferay.portlet.journal.model.JournalArticle";
	private static String UPDATE_JC_SQL = "update ResourcePermission set roleId=" + StartupAction.JOURNAL_ARTICLE_OWNER_ROLE_ID;
	private static String WHERE_JC_SQL = " where name='com.liferay.portlet.journal.model.JournalArticle' and scope=4 and actionIds=119 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    MBCATEGORY_ACTIONID = 4063;
	private static String MBCATEGORY_CLASSNAME = "com.liferay.portlet.messageboards.model.MBCategory";
	private static String UPDATE_MBCATEGORY_SQL = "update ResourcePermission set roleId=" + StartupAction.MBCAT_OWNER_ROLE_ID;
	private static String WHERE_MBCATEGORY_SQL=" where name='com.liferay.portlet.messageboards.model.MBCategory' and scope=4 and actionIds=4063 and roleId in " +
													"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    WIKINODE_ACTIONID = 173;
	private static String WIKINODE_CLASSNAME = "com.liferay.portlet.wiki.model.WikiNode";
	private static String UPDATE_WIKINODE_SQL = "update ResourcePermission set roleId=" + StartupAction.WIKI_NODE_OWNER_ROLE_ID;
	private static String WHERE_WIKINODE_SQL=" where name='com.liferay.portlet.wiki.model.WikiNode' and scope=4 and actionIds=173 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    WIKIPAGE_OWNER_ACTIONID = 119;
	private static String WIKIPAGE_CLASSNAME = "com.liferay.portlet.wiki.model.WikiPage";
	private static String UPDATE_WIKIPAGE_OWNER_SQL = "update ResourcePermission set roleId=" + StartupAction.WIKI_PAGE_OWNER_ROLE_ID;
	private static String WHERE_WIKIPAGE_OWNER_SQL=" where name='com.liferay.portlet.wiki.model.WikiPage' and scope=4 and actionIds=119 and roleId in " +
														"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int WIKIPAGE_EDITOR_ACTIONID = 87;
	private static String UPDATE_WIKIPAGE_EDITOR_SQL = "update ResourcePermission set roleId=" + StartupAction.WIKI_PAGE_EDITOR_ROLE_ID;
	private static String WHERE_WIKIPAGE_EDITOR_SQL=" where name='com.liferay.portlet.wiki.model.WikiPage' and scope=4 and actionIds=87 and roleId in " +
														"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    BOOKMARKS_ACTIONID = 125;
	private static String BOOKMARKS_CLASSNAME = "com.liferay.portlet.bookmarks.model.BookmarksFolder";
	private static String UPDATE_BOOKMRKS_SQL = "update ResourcePermission set roleId=" + StartupAction.BOOKMARKS_OWNER_ROLE_ID;
	private static String WHERE_BOOKMARKS_SQL=" where name='com.liferay.portlet.bookmarks.model.BookmarksFolder' and scope=4 and actionIds=125 and roleId in " +
													"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    CALEVENT_ACTIONID = 53;
	private static String CALEVENT_CLASSNAME = "com.liferay.portlet.calendar.model.CalEvent";
	private static String UPDATE_CALEVENT_SQL = "update ResourcePermission set roleId=" + StartupAction.CAL_EVENT_OWNER_ROLE_ID;
	private static String WHERE_CALEVENT_SQL=" where name='com.liferay.portlet.calendar.model.CalEvent' and scope=4 and actionIds=53 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    ASSETTAG_ACTIONID = 1;
	private static String ASSETTAG_CLASSNAME = "com.liferay.portlet.asset.model.AssetTag";
	private static String UPDATE_ASSETTAG_SQL = "update ResourcePermission set roleId=" + StartupAction.ASSETTAG_OWNER_ROLE_ID;
	private static String WHERE_ASSETTAG_SQL=" where name='com.liferay.portlet.asset.model.AssetTag' and scope=4 and actionIds=1 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static int    ASSETTAG_EDITOR_ACTIONID = 15;
	private static String UPDATE_ASSETTAG_EDITOR_SQL = "update ResourcePermission set roleId=" + StartupAction.ASSETTAG_EDITOR_ROLE_ID;
	private static String WHERE_ASSETTAG_EDITOR_SQL=" where name='com.liferay.portlet.asset.model.AssetTag' and scope=4 and actionIds=15 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Regular %')";
	
	private static String UPDATE_ASSETTAG_GROUP_SQL = "update ResourcePermission set roleId=" + StartupAction.ASSETTAG_GROUP_ROLE_ID;
	private static String WHERE_ASSETTAG_GROUP_SQL=" where name='com.liferay.portlet.asset.model.AssetTag' and scope=4 and actionIds=1 and roleId in " +
												"(select roleId from Role_ where description like 'Auto%' and name like 'Community %')";
	
	
	/**
	 * SQL statements to update Users_Roles table and Groups_Roles table
	 */
	
	private static String UPDATE_USERS_ROLES = "update Users_Roles set roleId=" + "<new_roleId>" + " where roleId = " + "<existing_roleId>"; 
	private static String UPDATE_GROUPS_ROLES = "update Groups_Roles set roleId=" + "<new_roleId>" + " where roleId = " + "<existing_roleId>"; 
	
	private static String DELETE_ROLES ="delete from Role_ where description like 'Auto%'";
	private static String DELETE_ROLES_FROM_USERS_ROLES ="delete from Users_Roles where roleId = ";
	private static String DELETE_ROLES_FROM_GROUPS_ROLES ="delete from Groups_Roles where roleId = ";
	
	
	
	private final static Log _log = LogFactoryUtil.getLog(StartupAction.class);
}
