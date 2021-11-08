package io.shelang.aghab.service.script;

import io.quarkus.panache.common.Page;
import io.shelang.aghab.domain.Script;
import io.shelang.aghab.domain.ScriptUser;
import io.shelang.aghab.repository.ScriptRepository;
import io.shelang.aghab.repository.ScriptUserRepository;
import io.shelang.aghab.service.mapper.ScriptMapper;
import io.shelang.aghab.service.script.dto.ScriptDTO;
import io.shelang.aghab.util.NumberUtil;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ScriptServiceImpl implements ScriptService {

  @Inject ScriptRepository scriptRepository;
  @Inject ScriptUserRepository scriptUserRepository;
  @Inject ScriptMapper scriptMapper;

  @Inject
  @Claim(standard = Claims.sub)
  Long userId;

  public static ScriptDTO from(RowSet<Row> rows) {
    var result = new ScriptDTO();
    for (var row : rows) {
      result = new ScriptDTO()
          .setId(row.getLong("id"))
          .setName(row.getString("name"))
          .setTimeout(row.getInteger("timeout"))
          .setTitle(row.getString("title"))
          .setContent(row.getString("content"));
    }
    return result;
  }

  @Override
  public ScriptDTO getById(Long id) {
    return scriptMapper.toDTO(getValidatedScript(id));
  }

  @Override
  public List<ScriptDTO> get(String name, Integer page, Integer size) {
    page = NumberUtil.normalizeValue(page, 1) - 1;
    size = NumberUtil.normalizeValue(size, 10);

    if (size > 50) size = 50;

    return scriptMapper.toDTO(scriptRepository.search(name, userId, Page.of(page, size)));
  }

  @Override
  @Transactional
  public ScriptDTO create(ScriptDTO dto) {
    Script script = scriptMapper.toEntity(dto);
    scriptRepository.persistAndFlush(script);
    saveScriptUser(script);
    return scriptMapper.toDTO(script);
  }

  @Override
  @Transactional
  public ScriptDTO update(ScriptDTO dto) {
    Script script = getValidatedScript(dto.getId());
    script.setTitle(dto.getTitle());
    script.setContent(dto.getContent());
    scriptRepository.persistAndFlush(script);
    saveScriptUser(script);
    return scriptMapper.toDTO(script);
  }

  private Script getValidatedScript(Long id) {
    ScriptUser scriptUser = getScriptUser(id).orElseThrow(ForbiddenException::new);
    Script script = scriptRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    if (!userId.equals(scriptUser.getUserId())) throw new ForbiddenException();
    return script;
  }

  private Optional<ScriptUser> getScriptUser(Long id) {
    return scriptUserRepository.findByIdOptional(makeScriptUser(id).getId());
  }

  @Transactional
  private void saveScriptUser(Script script) {
    ScriptUser scriptUser = makeScriptUser(script.getId());
    Optional<ScriptUser> exist = scriptUserRepository.findByIdOptional(scriptUser.getId());
    if (exist.isEmpty()) scriptUserRepository.persistAndFlush(scriptUser);
  }

  private ScriptUser makeScriptUser(Long id) {
    return new ScriptUser().setScriptId(id).setUserId(userId).setId(getScriptUserId(id));
  }

  private ScriptUser.ScriptUserId getScriptUserId(Long id) {
    return new ScriptUser.ScriptUserId(userId, id);
  }
}
