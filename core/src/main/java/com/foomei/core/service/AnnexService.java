package com.foomei.core.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.common.web.FileRepository;
import com.foomei.core.dao.jpa.AnnexDao;
import com.foomei.core.entity.Annex;
import com.foomei.core.web.CoreThreadContext;

/**
 * 附件管理业务类.
 *
 * @author walker
 */
// Spring Service Bean的标识.
@Service
@Transactional(readOnly = true)
public class AnnexService extends JpaServiceImpl<Annex, String> {

  @Autowired
  private AnnexDao annexDao;
  @Autowired
  private FileRepository fileRepository;

  public Annex getByPath(String path) {
    return annexDao.findByPath(path);
  }

  @Transactional(readOnly = false)
  public void delete(Annex annex) {
    fileRepository.deleteRelevantByPath(annex.getPath());
    delete(annex.getId());
  }

  @Transactional(readOnly = false)
  public void deleteByObject(String objectId, String objectType) {
    List<Annex> annexs = findByObject(objectId, objectType);
    for (Annex annex : annexs) {
      delete(annex);
    }
  }

  @Transactional(readOnly = false)
  public Annex copy(Annex annex, String path, String objectId, String objectType) throws IOException {
    String filePath = fileRepository.storeByPath(annex.getPath(), path);

    Annex entity = new Annex();
    entity.setObjectId(objectId);
    entity.setObjectType(objectType);
    entity.setPath(filePath);
    entity.setName(annex.getName());
    entity.setType(annex.getType());
    entity.setCreateTime(new Date());
    entity.setCreator(CoreThreadContext.getUserId());
    save(entity);

    return entity;
  }

  @Transactional(readOnly = false)
  public Annex save(BufferedImage image, String fileName, String path, String objectId, String objectType)
          throws IOException {
    String ext = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ENGLISH);
    String filePath = fileRepository.storeByExt(image, path, ext);

    Annex entity = new Annex();
    entity.setObjectId(objectId);
    entity.setObjectType(objectType);
    entity.setPath(filePath);
    entity.setName(fileName);
    entity.setType(ext);
    entity.setCreateTime(new Date());
    entity.setCreator(CoreThreadContext.getUserId());
    save(entity);

    return entity;
  }

  @Transactional(readOnly = false)
  public Annex save(byte[] data, String fileName, String path, String objectId, String objectType)
          throws IOException {
    String ext = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ENGLISH);
    String filePath = fileRepository.storeByExt(data, path, ext);

    Annex entity = new Annex();
    entity.setObjectId(objectId);
    entity.setObjectType(objectType);
    entity.setPath(filePath);
    entity.setName(fileName);
    entity.setType(ext);
    entity.setCreateTime(new Date());
    entity.setCreator(CoreThreadContext.getUserId());
    save(entity);

    return entity;
  }

  @Transactional(readOnly = false)
  public Annex save(byte[] data, String fileName, String objectId, String objectType) throws IOException {
    String filePath = fileRepository.storeByFilename(data, fileName);

    Annex entity = new Annex();
    entity.setObjectId(objectId);
    entity.setObjectType(objectType);
    entity.setPath(filePath);
    entity.setName(FilenameUtils.getName(fileName));
    entity.setType(FilenameUtils.getExtension(fileName).toLowerCase(Locale.ENGLISH));
    entity.setCreateTime(new Date());
    entity.setCreator(CoreThreadContext.getUserId());
    save(entity);

    return entity;
  }

  public List<Annex> findByObject(final String objectId, final String objectType) {
    return annexDao.findAll(new Specification<Annex>() {
      public Predicate toPredicate(Root<Annex> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p1 = cb.equal(root.get(Annex.PROP_OBJECT_ID).as(String.class), objectId);
        Predicate p2 = cb.equal(root.get(Annex.PROP_OBJECT_TYPE).as(String.class), objectType);
        return cb.and(p1, p2);
      }
    });
  }

}
