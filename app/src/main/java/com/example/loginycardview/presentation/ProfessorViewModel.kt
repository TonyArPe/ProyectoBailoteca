import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginycardview.domain.GetProfessorsUseCase
import com.example.loginycardview.domain.Professor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfessorViewModel(private val getProfessorsUseCase: GetProfessorsUseCase) : ViewModel() {

    private val _professors = MutableStateFlow<List<Professor>>(emptyList())
    val professors: StateFlow<List<Professor>> get() = _professors

    fun loadProfessors() {
        viewModelScope.launch {
            _professors.value = getProfessorsUseCase()
        }
    }
}
