package io.shelang.aghab.service.script;

import io.shelang.aghab.domain.Script;
import io.shelang.aghab.domain.ScriptUser;
import io.shelang.aghab.repository.ScriptRepository;
import io.shelang.aghab.repository.ScriptUserRepository;
import io.shelang.aghab.service.dto.script.ScriptDTO;
import io.shelang.aghab.service.mapper.ScriptMapper;
import io.shelang.aghab.service.user.TokenService;
import io.shelang.aghab.util.PageUtil;
import io.shelang.aghab.util.StringUtil;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ScriptServiceImpl implements ScriptService {

  @Inject
  ScriptRepository scriptRepository;
  @Inject
  ScriptUserRepository scriptUserRepository;
  @Inject
  ScriptMapper scriptMapper;
  @Inject
  TokenService tokenService;

  public static ScriptDTO from(RowSet<Row> rows) {
    var result = new ScriptDTO();
    for (var row : rows) {
      result =
          new ScriptDTO()
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
    return scriptMapper.toDTO(getScript(id));
  }

  @Override
  public ScriptDTO getByIdAndValidation(Long id) {
    return scriptMapper.toDTO(getValidatedScript(id));
  }

  @Override
  public List<ScriptDTO> get(String name, Integer page, Integer size) {
    return scriptMapper.toDTO(
        scriptRepository.search(name, tokenService.getAccessTokenUserId(),
            PageUtil.of(page, size)));
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
    if (StringUtil.nonNullOrEmpty(dto.getName())) {
      script.setName(dto.getName());
    }
    if (Objects.nonNull(dto.getTimeout()) && dto.getTimeout() > 0) {
      script.setTimeout(dto.getTimeout());
    }
    script.setTitle(dto.getTitle());
    script.setContent(dto.getContent());
    scriptRepository.persistAndFlush(script);
    saveScriptUser(script);
    return scriptMapper.toDTO(script);
  }

  private Script getValidatedScript(Long id) {
    ScriptUser scriptUser = getScriptUser(id).orElseThrow(ForbiddenException::new);
    Script script = scriptRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    if (!tokenService.getAccessTokenUserId().equals(scriptUser.getUserId())) {
      throw new ForbiddenException();
    }
    return script;
  }

  private Script getScript(Long id) {
    return scriptRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
  }

  private Optional<ScriptUser> getScriptUser(Long id) {
    return scriptUserRepository.findByIdOptional(makeScriptUser(id).getId());
  }

  private void saveScriptUser(Script script) {
    ScriptUser scriptUser = makeScriptUser(script.getId());
    Optional<ScriptUser> exist = scriptUserRepository.findByIdOptional(scriptUser.getId());
    if (exist.isEmpty()) {
      scriptUserRepository.persistAndFlush(scriptUser);
    }
  }

  private ScriptUser makeScriptUser(Long id) {
    return new ScriptUser()
        .setScriptId(id)
        .setUserId(tokenService.getAccessTokenUserId())
        .setId(getScriptUserId(id));
  }

  private ScriptUser.ScriptUserId getScriptUserId(Long id) {
    return new ScriptUser.ScriptUserId(tokenService.getAccessTokenUserId(), id);
  }
}
