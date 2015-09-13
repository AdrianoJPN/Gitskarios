package com.alorma.gitlabsdk.security.bean;

/**
 * Created by a557114 on 13/09/2015.
 */
public class GitlabNamespace {
    /*
    "namespace": {
      "created_at": "2013-09-30T13: 46: 02Z",
      "description": "",
      "id": 3,
      "name": "Diaspora",
      "owner_id": 1,
      "path": "diaspora",
      "updated_at": "2013-09-30T13: 46: 02Z"
    }
     */

    public int id;
    public int owner_id;
    public String owner;
}
