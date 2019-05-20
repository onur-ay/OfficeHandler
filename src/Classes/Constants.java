package Classes;

import javafx.scene.control.Tooltip;

public interface Constants {
    int TURKISH = 1;
    int ENGLISH = 2;
    int LANG = 1;
    int DOUBLE_CLICK = 2;
    boolean BFS = false;
    boolean DFS = true;
    boolean DONTCREATEFILE = false;
    boolean CREATEFILE = true;

    //COLORS
    String DEFAULT_FIRST_COLOR = "#2D2D2D";
    String DEFAULT_SECOND_COLOR = "#FFFF8D";
    String DEFAULT_FIRST_TEXT_COLOR = "#B2B2B2";

    //PROGRAM CONFIGURATIONS
    String PROGRAM_NAME = "Office Handler";
    String PROGRAM_NAME_TR = "Ofis Düzenleyicisi";
    String PROGRAM_ICON = "/resources/desktop-icon.png";
    String STYLE_SHEET_PATH = "/styles/dark-theme.css";
    String DESIGN_FILE = "Design.fxml";

    //ICONS
    String ALERT_BOX_ICON = "/resources/alertbox.png";
    String CONFIRM_BOX_ICON = "/resources/confirmbox.png";

    //DIALOG TEXTS
    String HELP_DIALOG_TEXT ="This program was created in the scope of term project of Yildiz Technical University Computer Engineering Department. "+
                                "All rights are reserved by Y.T.U. C.E. Mustafa ULUKAYA | Onur AY | 2019®\n\n"+
                                "Some hints and information to use program effectively:\n"+
                                "- Default value for \"Drives\" combo is \"All\".\n"+
                                "- Default value for \"File Types\" combo is \"All\".\n"+
                                "- Default value for \"Keyword\" text is empty string.\n"+
                                "- Default value for \"Match Case\" check-box is false.\n"+
                                "- Default value for \"Algorithm\" is BFS(Breadth-First Search)\n"+
                                "- To enable Match Case check-box you should enter keyword first.\n"+
                                "- \"Add to Favorites\" button adds a selected file to \"Favorites\" table.\n"+
                                "- \"Scan Tables\" button scans tables/founded files with filter values which you entered.\n"+
                                "- It is advised to analyze algorithms first to execute scanning with better performance.\n"+
                                "- You can scan every file type you want by selecting \"Add new file type\" from file types.\n"+
                                "- \"Scan Drives\" button scans computer and found files with filter values which you entered.\n"+
                                "- There are two algorithms; \"DFS\" for Depth-First-Search and \"BFS\" for Breadth-First Search\n"+
                                "- You can change algorithm whenever you want it will be apply on next search after change.\n"+
                                "- After analyzing algorithms program sets Algorithm combo automatically to best algorithm.\n"+
                                "- \"Clear Filters\" button clears all the areas except Algorithm combo and loads tables without filter.\n"+
                                "- You can make your computer scan in wherever you want by selecting \"Specific Directory\" from drives.\n"+
                                "- Colors with optimum beauty are chosen by default and you can restore by clicking \"Reset\" on color palette.\n"+
                                "- If you got bored at colors of program, you can change easily by clicking color icon right on top-right of screen.\n";
    String HELP_DIALOG_TEXT_TR =    "Bu proje Yıldız Teknik Üniversitesi Bilgisayar Mühendisliği Bölümü Bilgisayar Projesi dersi kapsamında oluşturulmuştur. "+
                                    "Tüm hakları Y.T.Ü. B.M. tarafından saklıdır. Mustafa ULUKAYA | Onur AY | 2019®\n\n"+
                                    "Programı efektif bir şekilde kullanmak için bazı ipucu ve bilgiler:\n"+
                                    "- \"Sürücüler\" alanının varsayılan değeri \"Hepsi\"dir.\n"+
                                    "- \"Anahtar Kelime\" alanının varsayılan değeri boştur.\n"+
                                    "- \"Dosya Tipleri\" alanının varsayılan değeri \"Hepsi\"dir.\n"+
                                    "- \"Büyük/Küçük Harf Uyumu\" kontrol alanının varsayılan değeri boştur.\n"+
                                    "- \"Favorilere Ekle\" butonu seçilen bir dosyayı \"Favoriler\" tablosuna ekler.\n"+
                                    "- \"Algorithm\" alanının varsayılan değeri BFS(Enlemesine-Öncelikli Arama)\n"+
                                    "- En iyi performans için tarama yapmadan önce algoritma analizi yapılması önerilir.\n"+
                                    "- \"Tabloları Tara\" butonu tabloları/bulunan dosyaları girilen filtre değerlerine göre filtreler.\n"+
                                    "- \"Sürücüleri Tara\" butonu bilgisayarı tarar ve girilen filtre değerlerine uygun dosyaları bulur.\n"+
                                    "- Büyük/Küçük harf uyumu kontrolü alanını aktifleştirmek için anahtar kelime girişi zorunludur.\n"+
                                    "- Algoritmayı ne zaman isterseniz değiştirebilirsiniz, değişikliğiniz bir sonraki taramada aktif olacaktır.\n"+
                                    "- Algoritma analizinin ardından program otomatik olarak en hızlı olan algortimayı seçer ve aktive eder.\n"+
                                    "- \"Filtreleri Temizle\" butonu Algoritma alanı hariç tüm alanları temizler ve tabloları filtresiz şekilde yükler.\n"+
                                    "- Dosya tipleri alanından \"Yeni Dosya Tipi Ekle\"yi seçerek istediğiniz herhangi bir dosya tipini arayabilirsiniz.\n"+
                                    "- Bilgisayar taramanızı, sürücüler alanından \"Belirli Dizin\" seçerek istediğiniz herhangi bir dizinde yapabilirsiniz.\n"+
                                    "- Optimum güzellikte ve sadelikte renkler varsayılan seçilmiştir. İstediğinizde \"Sıfırla\" tıklayarak geri yükleyebilirsiniz.\n"+
                                    "- \"DFS\"(Derinlemesine-Öncelikli Arama) ve \"BFS\"(Enlemesine-Öncelikli Arama) şeklinde iki algoritma bulunmaktadır.\n"+
                                    "- Eğer programın renklerinden sıkıldıysanız, sağ-üst köşede bulunan renk butonuna tıklayıp kolayca değiştirebilirsiniz.\n";
    String SCAN_IS_DONE_TEXT = "Scan is completed successfully !";
    String SCAN_IS_DONE_TEXT_TR = "Tarama başarıyla tamamlandı !";
    String ADD_FAV_TEXT = "Selected file is added to the Favorites !";
    String ADD_FAV_TEXT_TR = "Seçili dosya Favorilere eklendi !";
    String CANT_ADD_FAV_TEXT = "You can not add a file which has already exist in the Favorites !";
    String CANT_ADD_FAV_TEXT_TR = "Zaten favorilerde bulunan bir dosyayı ekleyemezsiniz !";
    String SELECT_FAV_TEXT = "Please select a file to add to Favorites !";
    String SELECT_FAV_TEXT_TR = "Lütfen FAvorilere eklemek için bir dosya seçiniz !";
    String ANALYZE_FINISH_TEXT = "Analyze is finished, results are on the left of screen !";
    String ANALYZE_FINISH_TEXT_TR = "Analiz sonlandı, sonuçları ekranın sağında inceleyebilirsiniz !";
    String FILE_NOT_FOUND_TEXT = "File not found. The file you are trying to open may be deleted or moved.";
    String FILE_NOT_FOUND_TEXT_TR = "Dosya bulunamadı. Açmaya çalıştığınız dosya silinmiş veya taşınmış olabilir.";
    String FILE_DOES_NOT_EXISTS_IN_DB_TEXT = "The file you tried to delete does not exist in DB !";
    String FILE_DOES_NOT_EXISTS_IN_DB_TEXT_TR = "Silmeye çalıştığınız dosya veritabanında bulunmamaktadır !";
    String EXISTING_DIRECTORY_TEXT = "You can not add an existing directory !";
    String EXISTING_DIRECTORY_TEXT_TR = "Zaten varolan bir dizin ekleyemezsiniz !";
    String NEW_FILE_TYPE_TEXT = "New File Type";
    String NEW_FILE_TYPE_TEXT_TR = "Yeni Dosya Tipi";
    String EMPTY_FILE_TYPE_TEXT = "You can not add an empty file type !";
    String EMPTY_FILE_TYPE_TEXT_TR = "Boş dosya tipi giremezsiniz !";
    String FILE_TYPE_DESCRIPTION_TEXT = "Please write the file type without dot before it. (Ex.: exe, txt etc.)";
    String FILE_TYPE_DESCRIPTION_TEXT_TR = "Lütfen dosya tipini başında nokta olmadan giriniz. (Örn.: exe, txt v.b.)";
    String EXISTING_FILE_TYPE_TEXT = "You can not add a file type which has already exists in the list !";
    String EXISTING_FILE_TYPE_TEXT_TR = "Zaten listede yer alan bir dosya tipini ekleyemezsiniz !";

    //TOOLTIPS
    Tooltip MATCH_CASE_TOOLTIP = new Tooltip("Please enter keyword to enable.");
    Tooltip MATCH_CASE_TOOLTIP_TR = new Tooltip("Lütfen aktifleştirmek için anahtar kelime giriniz.");
    Tooltip ALGORITHM_TOOLTIP = new Tooltip("It is advised to analyze algorithms first to make searches with optimum performance.");
    Tooltip ALGORITHM_TOOLTIP_TR = new Tooltip("Aramaları en iyi performansla yapabilmek adına ilk önce algortima analizi yapılması önerilir.");

    //LABEL TEXTS
    String TOTAL_FILES_TEXT = "Total Files: ";
    String TOTAL_FILES_TEXT_TR = "Toplam Dosya: ";
    String FILES_DONE_TEXT = "Files Done: ";
    String FILES_DONE_TEXT_TR = "Tamamlanan: ";
    String SELECT_SPECIFIC_DIRECTORY_TEXT = "Select specific directory...";
    String SELECT_SPECIFIC_DIRECTORY_TEXT_TR = "Belirli dizin seçiniz...";
    String ITEM_ALL_TEXT = "All";
    String ITEM_ALL_TEXT_TR = "Hepsi";
    String ADD_NEW_FILE_TYPE_TEXT = "Add new file type...";
    String ADD_NEW_FILE_TYPE_TEXT_TR = "Yeni dosya tipi ekle...";
    String DFS_DURATION_TEXT = "DFS Duration: ";
    String DFS_DURATION_TEXT_TR = "DFS Süre: ";
    String BFS_DURATION_TEXT = "BFS Duration: ";
    String BFS_DURATION_TEXT_TR = "BFS Süre: ";
    String WAITING_FOR_RESULTS_TEXT = "waiting for results...";
    String WAITING_FOR_RESULTS_TEXT_TR = "sonuçlar için bekleniyor...";
    String DFS_TEXT = "DFS";
    String DFS_TEXT_TR = "DFS";
    String BFS_TEXT = "BFS";
    String BFS_TEXT_TR = "BFS";
    String BACKGROUND_COLOR_TEXT = "Background Color";
    String BACKGROUND_COLOR_TEXT_TR = "Arkaplan Rengi";
    String BORDER_COLOR_TEXT = "Border Color";
    String BORDER_COLOR_TEXT_TR = "Kenarlık Rengi";
    String TEXT_COLOR_TEXT = "Text Color";
    String TEXT_COLOR_TEXT_TR = "Font Rengi";
    String OK_BUTTON_TEXT = "Confirm";
    String OK_BUTTON_TEXT_TR = "Tamam";
    String RESET_BUTTON_TEXT = "Reset";
    String RESET_BUTTON_TEXT_TR = "Sıfırla";

    //PATTERNS
    String DATE_PATTERN = "dd/MM/yyyy";
    String NUMBER_WITH_COMMA_PATTERN = "%,.0f";
    String DISPLAY_TIME_PATTERN = "%02d:%02d:%02d";
    String PROGRESS_INDICATOR_PATTERN = "%%%.0f";
    String PROGRESS_TEXT_PATTERN = "%.2f%%";

    //LOG TEXTS
    String LOG_UPDATE_FILE_DOES_NOT_EXIST_TEXT = "The file you tried to update does not exist in DB !";
    String LOG_EMPTY_DIRECTORY_TEXT = " is an empty directory!";
    String LOG_NOT_READABLE_DIRECTORY_TEXT = " is not a readable directory!";
    String LOG_NOT_APPROPRIATE_DIRECTORY_TEXT = " is not a appropriate directory!";
}
