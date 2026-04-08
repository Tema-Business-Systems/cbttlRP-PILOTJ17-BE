package com.transport.tracking.repository;


import com.transport.tracking.model.UserSite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSiteRepository extends CrudRepository<UserSite, String> {


    List<UserSite> findByUserOrderByFcynamAsc(String user);

    List<UserSite> findByUserAndFcyNumberOrderByFcynamAsc(String user,int fcyNumber);

}
