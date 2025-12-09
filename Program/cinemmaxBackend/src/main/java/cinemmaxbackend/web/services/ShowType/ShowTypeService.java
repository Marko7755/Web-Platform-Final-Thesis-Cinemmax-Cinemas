package cinemmaxbackend.web.services.ShowType;

import cinemmaxbackend.general.classes.classic.ShowType.ShowType;
import cinemmaxbackend.general.classes.commands.ShowType.ShowTypeCommand;
import cinemmaxbackend.general.classes.exceptions.DuplicateEntities.DuplicateShowTypeException;
import cinemmaxbackend.general.classes.exceptions.NotFound.ShowTypeNotFoundException;
import cinemmaxbackend.web.repositories.ShowType.ShowTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShowTypeService {
    private final ShowTypeRepository showTypeRepository;

    public List<ShowType> getAll() {
        return showTypeRepository.findAll();
    }


    public ResponseEntity<ShowType> getById(Long id) {
        Optional<ShowType> showTypeOpt = showTypeRepository.findById(id);
        return showTypeOpt.map(ResponseEntity::ok).orElseThrow(() -> new ShowTypeNotFoundException("Show type with id " + id + " not found"));
    }

    public ResponseEntity<Map<String, String>> add(ShowTypeCommand showTypeCommand) {
        if(checkDuplicate(showTypeCommand.getType())) {
            throw new DuplicateShowTypeException("Show type: " + showTypeCommand.getType() + " already exists");
        }

        ShowType showTypeToAdd = ShowType.convertToShowType(showTypeCommand);
        ShowType added = showTypeRepository.save(showTypeToAdd);

        if(added.getId() != null) {
            return ResponseEntity.ok(Map.of("message", "Show Type saved successfully"));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error saving ShowType"));
        }
    }

    private boolean checkDuplicate(String showType) {
        Optional<ShowType> opt = showTypeRepository.findByType(showType);
        return opt.isPresent();
    }

}
